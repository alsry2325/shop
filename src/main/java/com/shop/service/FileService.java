package com.shop.service;


import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {


    public String uploadFile(String uploadPath,String originalFileName,byte[] fileData)throws Exception{

        UUID uuid = UUID.randomUUID();  //UUID는 서로다른 개체들을 구별하기위해서  이름을 부여할때 사용
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension ;  //UUID로 받은 값과 원래 파일의 이름의 확장자를 조합해서 저장될 파일 이름을 만듬
        String fileUploadFullUrl = uploadPath + "/" +savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); //FileOutputStream 바이트단위의 출력을 내보내는 클래스 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만듬
        fos.write(fileData); //fileData를파일 출력 스트림에 입력
        fos.close();

        return savedFileName; //업로드된 파일의 이름을 반환합니다
    }

    public void deleteFile(String filePath)throws Exception{

        File deleteFile = new File(filePath); // 파일이  저장된 경로를 이용하여 파일 객체를 생성합니다

        if (deleteFile.exists()){ //해당파일이 존재하면 삭제함
            deleteFile.delete();
            log.info("파일을 삭제함");
        }else{
            log.info("파일이 존재하지않음");
        }

    }
}
