package com.shop.exception;

// 상품의 주문 수량보다 재고의 수가 적을 때 발생시킬 exception 정의
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }

}
