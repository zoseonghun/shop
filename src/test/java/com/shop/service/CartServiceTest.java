package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.CartItem;
import com.shop.entity.CartItemDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.yml")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository  cartItemRepository;

    // 장바구니에 담을 상품을 저장하는 메서드
    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(1000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }
    // 장바구니에 담을 회원정보를 저장하는 메서드
    // saveMember() 메서드에서 이메일을 유니크하게 생성하도록 수정
    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test+" + UUID.randomUUID() + "@test.com"); // 고유 이메일 생성
        return memberRepository.save(member);
    }

//    public Member saveMember() {
//        Member member = new Member();
//        member.setEmail("test@test.com");
//        return memberRepository.save(member);
//    }



    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart() {

        Item item = saveItem();
        Member member = saveMember();

        // 장바구니에 담을 상품,수량을 cartItemDto 객체에 세팅
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        // 상품을 장바구니에 담는 로직 호출 결과 생성된 장바구니 상품 아이디를 cartItemld 변수에 저장
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        // 장바구니 상품 아이디를 이용하여 생성된 장바구니 상품 정보를 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        // 상품 아이디와 장바구니에 저장된 상품 아이디가 같으면 테스트 성공
        assertEquals(item.getId(), cartItem.getItem().getId());

        // 장바구니에 담았던 수량과 실제로 장바구니에 저장된 수량이 같으면 테스트 성공
        assertEquals(cartItemDto.getCount(), cartItem.getCount());

    }

}