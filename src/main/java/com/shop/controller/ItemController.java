package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
     * 상품 조회 뷰
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){

        try {
               ItemFormDto itemFormDto = itemService.getItemDtl(itemId); //조회한 상품 데이터를 모델에 담아서 뷰로 전달
               model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){  //상품의 엔티티가 존재하지않을경우 에러메세지를 담아서 상품 등록 페이지로 이동
                model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
                model.addAttribute("itemFormDto", new ItemFormDto());
                return "item/itemForm";
        }

        return "item/itemForm";
    }

    /**
     * 상품 수정api
     * @param itemFormDto
     * @param bindingResult
     * @param itemImgFileList
     * @param model
     * @return
     */
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,BindingResult bindingResult, @RequestParam("itemImgFile")List<MultipartFile>
                             itemImgFileList,Model model){
        if (bindingResult.hasErrors()){
            return  "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다");
            return  "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정중 에러가 발생함");
            return "item/itemForm";
        }
        return  "redirect:/";
    }

    /**
     * 상품관리
     * @param itemSearchDto
     * @param page
     * @param model
     * @return
     */
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) //상품관리 화면 진입 시 URL에 페이지 번호가 없는 경우와 페이지 있는경우 2가지 맵핑
    public String itemManage(ItemSearchDto itemSearchDto , @PathVariable("page")Optional<Integer> page,Model model){
                Pageable pageable = PageRequest.of(page.isPresent() ?page.get() : 0,3); //PageRequest.of 메소드를 통해 Pageble 객체를 생성 첫번째 파라미터로는 조회할
        //페이지번호,두번째 파라미터로는  한번에 가져올데이터 수를 넣어줌 URL 경로에 페이지번호가 있으면해당 페이지 조회 페이지번호가 없으면 0페이지
        Page<Item> items =
                itemService.getAdminItemPage(itemSearchDto, pageable);//조회조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 객체를 반환받음
                model.addAttribute("items",items); //조회한 상품 데이터 및 페이징 정보를 뷰에 전달
                model.addAttribute("itemSearchDto",itemSearchDto); //페이지 전환시 기존 검색을  유지한채  이동할수 있도록 뷰에 다시 전달
                model.addAttribute("maxPage",5); //상품관리 메뉴하단에  보여줄 페이지 번호의 최대 개수

        return "item/itemMng";
    }


}
