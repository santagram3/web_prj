package com.project.web_prj.board.controller;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.board.repository.BoardMapper;
import com.project.web_prj.board.service.BoardService;
import com.project.web_prj.common.paging.Page;
import com.project.web_prj.common.paging.PageMaker;
import com.project.web_prj.common.search.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 게시물 목록요청: /board/list: GET
 * 게시물 상세조회요청: /board/content: GET
 * 게시글 쓰기 화면요청: /board/write: GET
 * 게시글 등록요청: /board/write: POST
 * 게시글 삭제요청: /board/delete: GET
 * 게시글 수정화면요청: /board/modify: GET
 * 게시글 수정요청: /board/modify: POST
 */


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


    // 게시물 목록 요청
    @GetMapping("/list")
    public String list(@ModelAttribute("s") Search search, Model model) {
        log.info("controller request /board/list GET! -{}", search);

        Map<String, Object> boardMap = boardService.findAllService(search);
        PageMaker pm = new PageMaker(new Page(search.getPageNum(), search.getAmount()), (Integer) boardMap.get("tc"));

        model.addAttribute("bList", boardMap.get("bList"));
        model.addAttribute("pm", pm);
        return "board/board-list";

    }


    // 게시물 상세 조회 요청
    @GetMapping("/content/{boardNo}")
    public String content(@PathVariable Long boardNo, Model model,
                          HttpServletResponse response, HttpServletRequest request, @ModelAttribute("p") Page page) {

        log.info("controller request /board/content GET! - {}", boardNo);
        Board board = boardService.findOneService(boardNo, response, request);
        log.info("return data - {}", board);
        model.addAttribute("b", board);
//        model.addAttribute("p",page);

        return "board/board-detail";
    }


    // 게시물 쓰기 화면 요청
    @GetMapping("/write")
    public String write(HttpSession session ,RedirectAttributes ra) {
        if (session.getAttribute("loginUser")==null){
            ra.addFlashAttribute("warningMsg","forbidden");
            return "redirect:/member/sign-in";
        }
        log.info("controller request /board/write GET!");
        return "board/board-write";
    }

    // 게시물 등록 요청
    @PostMapping("/write")
    public String write(Board board,
                        @RequestParam("files") List<MultipartFile> fileList
            , RedirectAttributes ra) {
        log.info("controller request /board/write POST! - {}", board);

//      if (fileList != null) {
//            List<String> fileNames = new ArrayList<>();
//            for (MultipartFile f : fileList) {
//                log.info("attachmented file-name: {}", f.getOriginalFilename());
//                fileNames.add(f.getOriginalFilename());
//            }
//            // board객체에 파일명 추가
//            board.setFileNames(fileNames);
//        }

        boolean flag = boardService.saveService(board);
        // 게시물 등록에 성공하면 클라리언트에 성공 메세지 전송
//        if (flag) model.addAttribute("msg","reg-success");
        // 모델은 결국 리퀘스트에 담기때문에 리 다이렉트 하면 사라진다 .
        // 그래서 나온 거 // 리다이렉트 해도 들고 가는거
        if (flag) ra.addFlashAttribute("msg", "reg-success");
        return flag ? "redirect:/board/list" : "redirect:/";
    }

    // 게시물 삭제 요청
    @GetMapping("/delete")
    public String delete(Long boardNo) {
        log.info("controller request /board/delete GET! - bno: {}", boardNo);

        return boardService.removeService(boardNo)
                ? "redirect:/board/list" : "redirect:/";
    }

    // 수정 화면 요청
    @GetMapping("/modify")
    public String modify(Long boardNo, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.info("controller request /board/modify GET! - bno: {}", boardNo);
        Board board = boardService.findOneService(boardNo, response, request);
        log.info("find article: {}", board);

        model.addAttribute("board", board);
        return "board/board-modify";
    }

    // 수정 처리 요청
    @PostMapping("/modify")
    public String modify(Board board) {
        log.info("controller request /board/modify POST! - {}", board);
        boolean flag = boardService.modifyService(board);
        return flag ? "redirect:/board/content/" + board.getBoardNo() : "redirect:/";
    }

    // 특정 게시물에 붙은 첨부파일 경로리스트를 클라리언트에게 비동기 전송
    @GetMapping("/file/{bno}")
    @ResponseBody
    public ResponseEntity<List<String>> getFiles(@PathVariable long bno){
        List<String> files = boardService.getFilesService(bno);
        log.info("board/file/{} get async {}",bno,files);

        return new ResponseEntity<>(files, HttpStatus.OK);
    }


//    //서버에 있는 파일 삭제 요청처리
//    //URI: /deleteFile?fileName=/2019/09/22/s_djfksldfjs_abc.jpg
//    @DeleteMapping("/deleteFile")
//    public ResponseEntity<String> deleteFile(String fileName) throws Exception {
//
//        try {
//            //파일 삭제
//            File delFile = new File(UPLOAD_PATH + fileName);
//            if (!delFile.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//            delFile.delete();
//
//            return new ResponseEntity<>("file-de-lsuccess", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }




}
