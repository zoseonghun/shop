package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        // 해당 상품의 이미지를 조회(등록순으로 가지고 오기 위해 상품 이미지 아이디 오름차순으로 가지고 옴)
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품의 아이디를 통해 상품 엔티티를 조회(존재하지 않을 때 EntityNotFoundException 발생)
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 수정
        // 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티 조회
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        //상품 등록 화면으로부터 전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트
        item.updateItem(itemFormDto);
        // 상품 이미지 아이디 리스트를 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        // 이미지 등록
        for(int i=0;i<itemImgFileList.size();i++) {
            // 상품 이미지를 업데이트하기 위해서 updateItemImg()메소드에 상품 이미지, 아이디, 상품 이미지 파일 정보를 파라미터로 전달
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();

    }

//    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
//
//        System.out.println("========== updateItem 시작 ==========");
//        System.out.println("상품 ID: " + itemFormDto.getId());
//
//        // 상품 수정
//        Item item = itemRepository.findById(itemFormDto.getId())
//                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
//
//        System.out.println("상품 조회 성공: " + item.getItemNm());
//
//        item.updateItem(itemFormDto);
//        System.out.println("상품 정보 업데이트 완료");
//
//        // DB에서 기존 이미지 목록을 직접 조회
//        List<ItemImg> existingItemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemFormDto.getId());
//        System.out.println("기존 이미지 개수: " + existingItemImgList.size());
//        System.out.println("업로드된 파일 개수: " + itemImgFileList.size());
//
//        // 이미지 업데이트
//        for (int i = 0; i < itemImgFileList.size(); i++) {
//            MultipartFile file = itemImgFileList.get(i);
//            System.out.println("처리 중: 파일 " + i + " - " + file.getOriginalFilename() + " (empty: " + file.isEmpty() + ")");
//
//            if (i < existingItemImgList.size()) {
//                // 기존 이미지 업데이트
//                Long itemImgId = existingItemImgList.get(i).getId();
//                System.out.println("  → 기존 이미지 업데이트 (ID: " + itemImgId + ")");
//                itemImgService.updateItemImg(itemImgId, file);
//            } else {
//                // 새 이미지 추가
//                System.out.println("  → 새 이미지 추가");
//                ItemImg newItemImg = new ItemImg();
//                newItemImg.setItem(item);
//                newItemImg.setRepimgYn("N");
//                itemImgService.saveItemImg(newItemImg, file);
//            }
//        }
//
//        System.out.println("========== updateItem 완료 ==========");
//        return item.getId();
//    }


}