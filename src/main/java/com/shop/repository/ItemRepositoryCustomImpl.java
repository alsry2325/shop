package com.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  ItemRepositoryCustom 사용자 정의 인터페이스 구현
 */
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{ // ItemRepositoryCustom상속


    private JPAQueryFactory queryFactory; //동적으로 쿼리를 생성하기 위해

    public ItemRepositoryCustomImpl(EntityManager em){ //queryFactory생성자로 EntityManager객체를 넣어줌
        this.queryFactory = new JPAQueryFactory(em);

    }

    private com.querydsl.core.types.dsl.BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){ //상품 판매 상태 조건이 null일 경우
        //null을 리턴함 결과값이 null이면 where절에서  해당조건은 무시됨 상품 판매 상태 조건이 null이 아니라 판매중 or품절상태 라면  해당 조건의  상품만 조회함
        return  searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);

    }

    private BooleanExpression regDtsAfter(String seachDateType){ //seachDateType값에 따라 dateTime의 값을  이전 시간의 값으로  세팅 후 해당 시간이후로
        //등록된 상품만 조회함 예를들어 seachDateType값이 1m 일경우 dateTime의 시간을 한 달전으로 세팅후  최근  한달동안 등록된 상품만 조회하도록  조건값을 반환
        LocalDateTime dateTime = LocalDateTime.now();


        if (StringUtils.equals("all",seachDateType) || seachDateType == null){
            return null;
        } else if (StringUtils.equals("1d",seachDateType)) {
                dateTime = dateTime.minusDays(1);
        }else if (StringUtils.equals("1w",seachDateType)) {
            dateTime = dateTime.minusWeeks(1);
        }else if (StringUtils.equals("1m",seachDateType)) {
            dateTime = dateTime.minusMonths(1);
        }else if (StringUtils.equals("6m",seachDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){ //searchBy값에 따라  상품명에 검색어를 포함하고 있는
        //상품 또는  상품 생성자의 아이디에  검색어를 포함하고있는  상품을 조회하도록  조건값을 반환

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        // queryFactory를 통해  쿼리 생성

        List<Item> content = queryFactory
                .selectFrom(QItem.item)    //상품 데이터를 조회하기위해 QItem의 item지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), //BooleanExpression반환하는 조건문들 넣어줌 ,단위를 넣을경우 and조건으로 인식
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()) //데이터를 가지고 올 시작 인덱스를 지정
                .limit(pageable.getPageSize()) //한번에 가지고 올  최대 개수를 지정
                .fetch(); //조회대상 리스트 반환

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne() //조회대상 한건이면 반환 그이상이면 에러
                ;

        return new PageImpl<>(content, pageable, total); //조회한 데이터를  Page 클래스의 구현체인  PageImpl 객체로 반환
    }


}
