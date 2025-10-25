package com.shop.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import com.shop.dto.ItemFormDto;

import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            // 조회한 상품 데이터를 모델에 담아서 뷰로 전달
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
            // 상품 엔티티가 존재하지 않을 경우 에러메시지를 담아서 상품 등록 페이지로 이동
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";

    }

//    @PostMapping(value = "/admin/item/{itemId}")
//    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
//                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
//                             Model model) {
//
//        System.out.println("==================== itemUpdate 시작 ====================");
//        System.out.println("itemId: " + itemFormDto.getId());
//        System.out.println("itemNm: " + itemFormDto.getItemNm());
//        System.out.println("itemImgIds: " + itemFormDto.getItemImgIds());
//        System.out.println("itemImgFileList size: " + itemImgFileList.size());
//
//        for (int i = 0; i < itemImgFileList.size(); i++) {
//            MultipartFile file = itemImgFileList.get(i);
//            System.out.println("파일 " + i + ": " + file.getOriginalFilename() + " (empty: " + file.isEmpty() + ")");
//        }
//
//        if(bindingResult.hasErrors()) {
//            System.out.println("Validation 에러 발생");
//            bindingResult.getAllErrors().forEach(error -> {
//                System.out.println("  - " + error.getDefaultMessage());
//            });
//            return "item/itemForm";
//        }
//
//        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
//            System.out.println("첫번째 이미지 필수 에러");
//            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
//            return "item/itemForm";
//        }
//
//        try {
//            System.out.println("updateItem 호출 전");
//            itemService.updateItem(itemFormDto, itemImgFileList);
//            System.out.println("updateItem 호출 성공");
//        } catch (Exception e) {
//            System.out.println("==================== 에러 발생 ====================");
//            System.out.println("에러 타입: " + e.getClass().getName());
//            System.out.println("에러 메시지: " + e.getMessage());
//            e.printStackTrace();
//            System.out.println("===================================================");
//
//            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다: " + e.getMessage());
//            model.addAttribute("itemFormDto", itemFormDto);
//            return "item/itemForm";
//        }
//
//        System.out.println("==================== itemUpdate 완료 ====================");
//        return "redirect:/";
//    }


}