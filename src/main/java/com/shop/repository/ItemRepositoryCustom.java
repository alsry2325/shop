package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Querydsl 사용자 정의 인터페이스 작성
 */
public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    //상품 조회 조건을 담고있는  itemSearchDto 객체와 페이징정보를 담고있는 pageble 객체를 파라미터로  받는
    //getAdminItemPage메소드를 정의함 반환 데이터로  page<Item>객체를 반환
}
