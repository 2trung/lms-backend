package com.example.LMS.exception;

import com.example.LMS.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e, WebRequest request) {
        String message = e.getMessage();
        ErrorResponse errorResponse;
        if (e instanceof MethodArgumentNotValidException) {
            message = Objects.requireNonNull(((MethodArgumentNotValidException) e).getBindingResult().getFieldError()).getDefaultMessage();
            errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, request, "Invalid Payload", message);
        } else if (e instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, request, "Invalid Parameter", message);
        } else if (e instanceof MissingServletRequestParameterException) {
            errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, request, "Invalid Parameter", message);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, request, "Invalid Parameter", message);
        } else {
            errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, request, "Invalid Data", message);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationFailedException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.UNAUTHORIZED, request, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND, request, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, request, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.UNAUTHORIZED, request, "Invalid Token");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.UNAUTHORIZED, request, "Token Expired");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND, request, e.getMessage() == null ? "Not Found" : e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.FORBIDDEN, request, "Access Denied");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "An unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, WebRequest request, String message) {
        return buildErrorResponse(status, request, status.getReasonPhrase(), message);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, WebRequest request, String error, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(status.value());
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        return errorResponse;
    }
}
