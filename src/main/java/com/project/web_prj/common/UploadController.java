package com.project.web_prj.common;

import com.project.web_prj.util.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
public class UploadController {



    // 업로드 파일 저장 경로
    private static final String UPLOAD_PATH = "C:\\code\\upload";


    // upload-form.jsp로 포워딩하는 요청
    @GetMapping("/upload-form")
    public String uploadForm() {
        return "upload/upload-form";
    }

    // 파일 업로드 처리를 위한 요청
    // MultipartFile: 클라이언트가 전송한 파일 정보들을 담은 객체
    // ex) 원본 파일명, 파일 용량, 파일 컨텐츠타입...
    @PostMapping("/upload")
    public String upload(@RequestParam("file") List<MultipartFile> fileList) {
//        @RequestParam("file")  업로드하는 폼에 name="file" 이런 게 있어서 그걸 그대로 받는데 그게
//        List<MultipartFile> fileList 라는 뜻
        log.info("/upload POST! - {}", fileList);

        for (MultipartFile file : fileList) {
            log.info("file-name: {}", file.getName());
            log.info("file-origin-name: {}", file.getOriginalFilename());
            log.info("file-size: {}KB", (double) file.getSize() / 1024);
            log.info("file-type: {}", file.getContentType());
            System.out.println("==================================================================");


            // 서버에 업로드파일 저장

//            // 업로드 파일 저장 경로
//            String uploadPath = "C:\\code\\upload";

            // 1. 세이브파일 객체 생성
            //  - 첫번째 파라미터는 파일 저장경로 지정, 두번째 파일명지정
        /*File f = new File(uploadPath, file.getOriginalFilename());

        try {
            file.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

            FileUtils.uploadFile(file, UPLOAD_PATH);
        }

        return "redirect:/upload-form";
    }

    // 비동기 전용 파일 업로드 처리
    @PostMapping("/ajax-upload")
    @ResponseBody
    public List<String> ajaxUpload(List<MultipartFile> files) {

        log.info("/ajax-upload post {}", files.get(0).getOriginalFilename());

        // 클라이언트에게 전송할 파일 경로 리스트
        List<String> fileNames = new ArrayList<>();


        // 클라이언트가 전송한 파일 업로드 하기
        for (MultipartFile file : files) {
            String fullPath = FileUtils.uploadFile(file, UPLOAD_PATH);
            fileNames.add(fullPath);
        }
        return fileNames;
    }

    // 파일 데이터 로드 요청 처리
    /*
    *  비동기 통신 응답시  ResponseEntity 쓰는 이유는  ?
    *  이 객체는 응답 body 정보 이외에도 header 정보를 포함 할수 있고
    *  추가로 응답 상태코드도 제어 할수있다
    * */

    // 파일 데이터 로드 요청 처리
    @GetMapping("/loadFile")
    @ResponseBody
    // fileName = /2022/08/01/변환된 파일명
    public ResponseEntity<byte[]> loadFile(String fileName) {
        log.info("loadFile path {}", fileName);

        // 클라이언트가 요청하는 파일의 진짜 버이트 데이터를 갖다 줘야 함

        //1. 요청파일 찾아서 file 객체로 포장
        File f = new File(UPLOAD_PATH+fileName);

        if(!f.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //2. 해당 파일을 InputStream을 통해 불러온다
        try (FileInputStream fis = new FileInputStream(f)){
            // 인풋스트림 , 아웃풋 스트림

            //3. 클라이엉ㄴ트에게 순수 이미지를 응답해야 하므로 MINE TYPE을 응답헤더에 설정
            // ex) image/jpeg , image/png , image/gif
            // 확장자를 추출해야 함 .
            String ext = FileUtils.getFileExtension(fileName);
            MediaType mediaType = FileUtils.getMediaType(ext);

            HttpHeaders headers = new HttpHeaders();

            if(mediaType !=null){// 이미지라면 ?
                headers.setContentType(mediaType);
            }
            // 4. 파일 순수 데이터 바이트 배열에 저장.
//            //파일업로드 라이브러리
//            implementation 'commons-io:commons-io:2.8.0'
//            //이미지 썸네일 라이브러리
//            implementation 'org.imgscalr:imgscalr-lib:4.2'
//             이거 그레이들 디펜던씨에 추가
            //IOUtils을  아파치 커몬 으로 임포트 받아야됨
            byte[] rawData = IOUtils.toByteArray(fis);


            // 5. 비동기통신에서 데이터 응답할때 ResponseEntity
            return new ResponseEntity<>(rawData,headers, HttpStatus.OK); // 클라이언트에 파일 데이터 응답.


        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}
