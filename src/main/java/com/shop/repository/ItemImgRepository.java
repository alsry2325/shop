package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    /**
     * 상품의 이미지를 조회
     */

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
