package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;
    /**
     * 상품 등록 뷰
     * @return
     */
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){

        model.addAttribute("itemFormDto",new ItemFormDto());
        return "/item/itemForm";
    }

    /**
     * 상품 등록 api
     * @param itemFormDto
     * @param bindingResult
     * @param model
     * @param itemImgFileList
     * @return
     */
    @PostMapping(value = "/admin/item/new")
    public String itemNew (@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        if (bindingResult.hasErrors()){  //상품 등록시 필수 값이 없다면 다시 상품등록 페이지로 전환됩니다
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){ //상품 등록시 첫번째 이미지가 없다면 에러메세지와 함께 상품 등록 페이지로 전환 상품의 첫번째 이미지는 메인페이지에  보여줄 상품 이미지로 사용하기위해서 필수 값으로 지정
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList); //상품 저장 로직을 호출 매개변수로 상품정보와 상품  이미지를 담고있는 itemlmgFileList를 넘겨줌
        } catch (Exception e) {
           model.addAttribute("errorMessage","상품 등록 중 에러가 발생하엿습니다");
            return "item/itemForm";
        }
        return "redirect:/";  //상품이 정상적으로 등록되면 메인페이지로 이동
    }


    /**
     * 상품 수정
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){

        try {
               ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
               model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){
                model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
                model.addAttribute("itemFormDto", new ItemFormDto());
                return "item/itemForm";
        }

        return "item/itemForm";
    }
}
