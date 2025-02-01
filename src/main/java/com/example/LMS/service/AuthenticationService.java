package com.example.LMS.service;

import com.example.LMS.constant.AccountProvider;
import com.example.LMS.constant.PredefinedRole;
import com.example.LMS.dto.request.LoginRequest;
import com.example.LMS.dto.request.RegisterRequest;
import com.example.LMS.dto.response.LoginResponse;
import com.example.LMS.entity.Role;
import com.example.LMS.entity.User;
import com.example.LMS.exception.AuthenticationFailedException;
import com.example.LMS.exception.NotFoundException;
import com.example.LMS.exception.UserNotFoundException;
import com.example.LMS.mapper.UserMapper;
import com.example.LMS.repository.RoleRepository;
import com.example.LMS.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements IAuthenticationService {
    @Value("${spring.security.oauth2.client.registration.google.user-info-endpoint-uri}")
    String googleUserInfoUrl;

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final AuthenticationManager authenticationManager;
    final JwtService jwtService;
    final UserMapper userMapper;
    final ClientRegistrationRepository clientRegistrationRepository;
    final PasswordEncoder passwordEncoder;

    final Gson gson = new Gson();

    public String generateLoginUrl(String provider) {
        if (provider.equalsIgnoreCase("google")) {
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
            String scope = String.join(" ", clientRegistration.getScopes());
            String redirectUri = clientRegistration.getRedirectUri();
            return clientRegistration.getProviderDetails().getAuthorizationUri() +
                    "?response_type=code" +
                    "&client_id=" + clientRegistration.getClientId() +
                    "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        }
        return null;
    }

    public LoginResponse socialAuthentication(String code, String provider) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
            String googleClientId = clientRegistration.getClientId();
            String googleClientSecret = clientRegistration.getClientSecret();
            String googleRedirectUri = clientRegistration.getRedirectUri();
            String googleAccessToken = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), new GsonFactory(),
                    googleClientId,
                    googleClientSecret,
                    code,
                    googleRedirectUri
            ).execute().getAccessToken();
            restTemplate.getInterceptors().add((req, body, executionContext) -> {
                req.getHeaders().set("Authorization", "Bearer " + googleAccessToken);
                return executionContext.execute(req, body);
            });
            String userInfoResponse = restTemplate.getForObject(googleUserInfoUrl, String.class);
            var googleUser = gson.fromJson(userInfoResponse, Map.class);
            String email = (String) googleUser.get("email");
            String avatar = (String) googleUser.get("picture");
            String name = (String) googleUser.get("name");
            String providerId = (String) googleUser.get("sub");
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null)
                user = register(RegisterRequest.builder().email(email).name(name).build(), avatar, AccountProvider.GOOGLE, providerId);
            else if (user.getProvider() != AccountProvider.GOOGLE)
                throw new AuthenticationFailedException("Email already registered with another provider");
            String accessToken = jwtService.generateToken(user);
            return LoginResponse.builder().user(userMapper.toUserResponse(user)).accessToken(accessToken).build();
        } catch (Exception e) {
            throw new AuthenticationFailedException((e.getMessage() != null) ? e.getMessage() : "Authentication failed");
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
            if (user.getProvider() != AccountProvider.LOCAL)
                throw new AuthenticationFailedException("Email registered with another provider");
            String accessToken = jwtService.generateToken(user);
            return LoginResponse.builder().user(userMapper.toUserResponse(user)).accessToken(accessToken).build();
        } catch (Exception e) {
            throw new AuthenticationFailedException((e.getMessage() != null) ? e.getMessage() : "Bad credentials");
        }
    }

    public void register(RegisterRequest request) {
        register(request, null, AccountProvider.LOCAL, null);
    }

    @Override
    public User register(RegisterRequest request, String avatar, AccountProvider provider, String providerId) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent())
                throw new AuthenticationFailedException("Email already registered");
            Role role = roleRepository.findByName(String.valueOf(PredefinedRole.USER)).orElseThrow(() -> new NotFoundException("Role not found"));
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            User user = User.builder()
                    .email(request.getEmail())
                    .password(hashedPassword)
                    .name(request.getName())
                    .provider(provider)
                    .providerId(providerId)
                    .avatar(avatar)
                    .roles(Set.of(role))
                    .build();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new AuthenticationFailedException((e.getMessage() != null) ? e.getMessage() : "Failed to register user");
        }
    }
}
