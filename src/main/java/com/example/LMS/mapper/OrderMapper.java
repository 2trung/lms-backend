package com.example.LMS.mapper;

import com.example.LMS.dto.response.CreateOrderResponse;
import com.example.LMS.dto.response.OrderResponse;
import com.example.LMS.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);
}
