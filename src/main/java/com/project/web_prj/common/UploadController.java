package com.project.web_prj.common;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Log4j2
public class UploadController {

    //upload-form.jsp로 포워딩 하는 요청
    @GetMapping("/upload-form")
    public String uploadForm() {
        return "upload/upload-form";
    }

    // 파일 업로드 처리를 위한 요청
    //  MultipartFile : 클라이언트가 전송한 파일 정보들을 담은 객체
    // ex) 원본 파일명 , 파일 용량 , 파일 컨텐츠 타입...
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        // file 인 이유는 , form name 이 file 이기 때문에
        log.info("/upload post {}",file);
        log.info(file.getName()); // file
        log.info(file.getOriginalFilename()); //  KakaoTalk_20220801_114933022.jpg
        log.info(file.getSize());//102875
        log.info(file.getContentType()); //image/jpeg
        log.info(file.isEmpty()); //false
        log.info(file.toString()); // 주소값
        //org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@6f7f5da7

        // 서버에 업로드 차일 저장

        // 업로드 파일 저장 경로
        String ulLoadPath= "C:\\code\\upload";
        // 1. 세이브 파일 객체 생성
        // - 첫번찌 파라미터는 파일 저장 경로 지정, 두번째 파일명 지정

        File f = new File(ulLoadPath, file.getOriginalFilename());
        try {
            file.transferTo(f);
        }catch (IOException e){
            e.printStackTrace();
        }

        return "redirect:/upload-form";
    }



    // 파일 업로드 진행 방식 !
    // 클라리언트 - WAS - DB
    //            FileServer


}
