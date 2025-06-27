package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

    // 상품 코드
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    // 상품명
    @Column(nullable = false, length = 50)
    private String itemNm;

    // 가격
    @Column(name = "price", nullable = false)
    private int price;

    // 재고수량
    @Column(nullable = false)
    private int stockNumber;

    // 상품 상세 설명 (긴 텍스트)
    @Lob
    @Column(nullable = false)
    private String itemDetail;

    // 상품 판매 상태 (Enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemSellStatus itemSellStatus;

    // 등록 시간
    private LocalDateTime regTime;

    // 수정 시간
    private LocalDateTime updateTime;
}
