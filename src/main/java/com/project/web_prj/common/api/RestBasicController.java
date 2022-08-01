package com.project.web_prj.common.api;


import com.project.web_prj.board.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

// 그냥 컨트롤러랑 다르다
// JSP 뷰 포워딩을 하지 않고 클라이언트에게 JSON 데이터를 전송함

@Log4j2
@RestController
public class RestBasicController {

    @GetMapping("/api/hello")
    public String hello(){
        return "hello!!!";
    }

    @GetMapping("/api/board")
    // 이거 안됨
    public Board board(){
        Board board = new Board();
        board.setBoardNo(10L);
        board.setWriter("작성자입니다.");
        board.setTitle("타이틀입니다.");
        board.setContent("컨텐트 입니다.");
        return board;
    }
    // post
    @PostMapping("/api/join")
    public String joinPost(@RequestBody String[] info){
        // 이상하게 옴
//        String[]
//        List<String>
        // 나오긴 나오는데 잘 물음표값으로 나옴

//       @RequestBody 를 안붙이면 자동으로 @RequestParam 이게 붙는다
//        [
//        "홍길동", "서울시", "30"
//          ]
        log.info("/api/join post {}",info );
        return "post";
    }
    @PutMapping("/api/join")
    // 이건 잘 나옴
    public String joinPut(@RequestBody Board board){
//        {
//            "boardNo" : 1
//                , "writer" : "망나니"
//                , "content" : "gkgkgkggkgkgkgkgkg"
//                , "title" : "제목~~"
//        }
        log.info("/api/join put {}",board);
        return "put";
    }

    // delete
    @DeleteMapping("/api/join")
    public String joinDelete(){
        log.info("/api/join delete");
        return "delete";
    }

    // RestController 에서 뷰포워딩 하기
    @GetMapping("/hoho")
    public ModelAndView hoho(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }








}
