package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {

    //상품 생성자
    private Long id;   //상품번호
    private String itemNm; //상품이름
    private Integer price;// 상품가격
    private String itemDetail;//상품 설명
    private String sellStatCd; //상품 판매 상태
    private LocalDateTime regTime; //등록시간
    private LocalDateTime updateTime; //수정시간


}
