package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {


    @PersistenceContext
    EntityManager em;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(100000);
        item.setItemDetail("상세설명 테스트 상품");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(10000);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }

    }
    public void createItemList2(){
        for (int i =1; i<=5; i++){
            Item item = new Item();
            item.setItemNm("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for (int i =6; i<= 10; i++){
           Item item = new Item();
           item.setItemNm("테스트상품" + i);
           item.setPrice(10000 + i);
           item.setItemDetail("테스트상품 상세설명" + i);
           item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
           item.setStockNumber(100);
           item.setRegTime(LocalDateTime.now());
           item.setUpdateTime(LocalDateTime.now());
           itemRepository.save(item);
        }
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명,상품 상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemNmOrItemDetail("테스트상품1", "테스트상품 설명5");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void  findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemDetail("테스트상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("NativeQuery 속성을 이용한 상품 조회 테스트")
    public  void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemDetailByNavive("테스트상품 상세 설명");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }


    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public  void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트상품 상세 설명"+"%"))
                .orderBy(qItem.price.desc());


        List<Item> itemList = query.fetch();

        for (Item item : itemList){
            System.out.println(item.toString());
        }


    }
    @Test
    @DisplayName("Querydsl 조회 테스트2")
    public void queryDslTest2(){

        this.createItemList2();
        // 쿼리에 들어간 조건을 만들어주는 빌더
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QItem item = QItem.item;
        String itemDetail = "테스트상품 상세 설명";
        int price = 10001;
        String itemSellStat = "SELL";
        //필요한 조건만 and  판매상태가 sell일때만  판매조건 동적으로 추가
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        //10001보다 큰거
        booleanBuilder.and(item.price.gt(price));

        //org.thymeleaf.util.StringUtils;
        //조건이 참일때 실행
        if (StringUtils.equals(itemSellStat,ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }
        //데이터를 페이징해 조회하려고 선언    첫번째인자는 조회할 페이지 번호, 두번째는 한페이지당 조회할 갯수
        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPageingResult =
                //findAll메소드를 사용해 조건에 맞는 데이터를 페이지 객체로 받아옴
                itemRepository.findAll(booleanBuilder,pageable);
                System.out.println("total elements : "+itemPageingResult.getTotalElements());

        List<Item> resultItemList = itemPageingResult.getContent();
        for (Item resultItem : resultItemList){
            System.out.println(resultItem.toString());
        }


    }


}