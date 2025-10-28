package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // 주문가격
    private int orderPrice;

    // 수량
    private int count;

    public static OrderItem createOrderItem(Item item, int count) {

        OrderItem orderItem = new OrderItem();
        // 주문한 상품과 주문 수량
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        // 주문 수량만큼 상품의 재고 수량 감소
        item.removeStock(count);
        return orderItem;

    }

    // 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메서드
    public int getTotalPrice() {
        return orderPrice * count;
    }

    // 주문 취소 시 주문 수량만큼 상품의 재고를 더해줌
    public void cancel() {
        this.getItem().addStock(count);
    }

}
