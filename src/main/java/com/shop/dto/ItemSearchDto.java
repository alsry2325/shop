package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;


/**
 * 상품 데이터 조회시 상품 조회 조건을 가지고있는 클래스
 *  조회 조건
 *
 * 상품 등록일
 * 상품 판매상태
 * 상품명 또는 상품 등록자 아이디
 *
 */
@Getter @Setter
public class ItemSearchDto {

    private String searchDateType; //현재 시간과  상품 등록일을 비교해서 상품 데이터를  조회합니다
    //all : 상품 등록일 전체
    //1d : 최근 하루동안 등록된  상품
    //1w: 최근 일주일 동안
    //1m: 최근 한달동안
    //6m: 최근 6개월동안

    private ItemSellStatus searchSellStatus; //상품의 판매상태를 기준으로 상품데이터를 조회

    private String searchBy; //상품 조회할때 어떤유형 조회할지
    //itemNm: 상품명
    //createdBy: 상품 등록자 아이디
    private String searchQuery = ""; //조회할 검색어 저장변수 searchBy가 itemNm일 경우 상품명을 기준으로 검색하고, createdBy일 경우
    //상품 등록자 아이디 기준으로 검색


}
