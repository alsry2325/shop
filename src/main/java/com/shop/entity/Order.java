package com.shop.entity;


import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity{


    @Id @GeneratedValue
    @Column(name = "order_id")
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩설정
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;  //주문일

    @Enumerated(EnumType.STRING)  //enum의 값을 index가 아닌 텍스트 값 그대로 저장하고 싶을 때 위의 어노테이션을 아래와 같이 entity에서 column을 정의해주는 곳에 붙여주면, DB에 enum의 값 자체가 텍스트 그대로 저장이 잘 된다.
    private OrderStatus orderStatus;  //주문상태

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,
                orphanRemoval = true ,fetch = FetchType.LAZY)  //부모 아래 자식 고아객체 제거
     private List<OrderItem> orderItems = new ArrayList<>();


  //  private LocalDateTime regTime;  //주문시간

   // private LocalDateTime updateTime; //주문수정시간


}
