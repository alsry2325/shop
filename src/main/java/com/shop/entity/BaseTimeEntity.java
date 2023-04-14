package com.shop.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})  
@MappedSuperclass     //공통 매핑 정보가 필요할때 사용하는 어노테이션 부모클라스를 상속받는 자식클래스한에서 매핑정보 제공
@Getter @Setter
public abstract class BaseTimeEntity {


    @CreatedDate   //엔티티가 생성되어 자동저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate  //값을 변경할때  시간 자동저장
    private LocalDateTime updateTime;


}
