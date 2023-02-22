package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {

    //상품명찾기
    List<Item> findByItemNm(String itemNm);
    //상품명 상품상세설명찾기
    List<Item> findByItemNmOrItemDetail(String itemNm,String itemDetail);
    //파라미터로 넘어온가격보다 작은 상품데이터 조회
    List<Item> findByPriceLessThan(Integer price);
    //가격 내림차순조회
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //@Query를 이용한 상품 조회
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "select i from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc",nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail")String itemDetail);

}
