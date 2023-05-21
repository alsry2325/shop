package com.shop.service;


import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor  //Lombok으로 스프링에서 DI(의존성 주입)의 방법 중에 생성자 주입을 임의의 코드없이 자동으로 설정해주는 어노테이션이다.
@Transactional  //데이터베이스를 다룰 때 트랜잭션을 적용하면 데이터 추가, 갱신, 삭제 등으로 이루어진 작업을 처리하던 중 오류가 발생했을 때 모든 작업들을 원상태로 되돌릴 수 있다. 모든 작업들이 성공해야만 최종적으로 데이터베이스에 반영하도록 한다.

public class ItemImgService {

    /**
     *  상품이미지  업로드, 상품이미지 정보 저장
     */

    @Value("${itemImgLocation}")    //벨루를 통해 application.properties 파일에 등록한 itemImgLocation 값을 불러와 변수에 넣음
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void  saveItemImg(ItemImg itemImg, MultipartFile itemImgFile)throws Exception{

        String oriImgName = itemImgFile.getOriginalFilename(); //업로드했던  상품 이미지  파일의 원래 이름
        String imgName =""; //실제 로컬에 저장되는 상품 이미지 파일의 이름
        String imgUrl =""; // 업로드 결과 로컬에 저장된  상품 이미지 파일을 불러오는 경로

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation,oriImgName,itemImgFile.getBytes()); //사용자가 이미지를 등록하면 저장할 경로와 파일의 이름,
            // 파일을 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드 호출  호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장
            imgUrl = "/images/item/" + imgName; //저장한 상품 이미지를 불러올 경로를 설정, 외부 리소스를 불러오는 urlPatterns로 WebMvcConfig 클래스에 "/images/**" 설정했음
            // 또한  application.properties에서 설정한 uploadPath 프로퍼티 경로인 "C:/shop/" 아래 item 폴더에 이미지를 저장하므로 상품이미지를 불러오는 경로로 "/images/item/" 붙여준다
        }


        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImgRepository.save(itemImg);


    }


}
