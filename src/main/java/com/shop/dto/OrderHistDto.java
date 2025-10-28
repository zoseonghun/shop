package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistDto {

    // 주문 아이디
    private Long orderId;

    // 주문 날짜
    private String orderDate;

    // 주문 상태
    private OrderStatus orderStatus;

    // 주문 상품 리스트
    private List <OrderItemDto> orderItemDtoList = new ArrayList<>();

    // order 객체를 파라미터로 받아서 멤버 변수 값을 세팅(주문 날짜의 경우 "yyyy-MM-dd HH:mm" 형태로 포맷 수정)
    public OrderHistDto(Order order) {

        this.orderId = orderId;
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = orderStatus;

    }

    // orderItemDto 객체를 주문 상품 리스트에 추가하는 메서드
    public void addOrderItemDto(OrderItemDto orderItemDto) {

        orderItemDtoList.add(orderItemDto);

    }
}
