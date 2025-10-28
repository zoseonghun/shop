package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
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
public class Item extends BaseEntity {

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

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구함
        int restStock = this.stockNumber - stockNumber;
        // 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외 발생
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. 현재 재고 수량 : " + this.stockNumber);
        }
        // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당
        this.stockNumber = restStock;
    }

    // 상품의 재고를 증가시키는 메서드
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}
