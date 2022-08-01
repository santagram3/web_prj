package com.project.web_prj.reply.api;


import com.project.web_prj.common.paging.Page;
import com.project.web_prj.reply.domain.Reply;
import com.project.web_prj.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/replies") // v1 = 초기버전
@CrossOrigin // 다른 브라우저 , 다른 곳에서 접속 가능 하게 만들어주는  // 외부 서버에서 갔다 쓸수 있음
public class ReplyApiController {

    private final ReplyService replyService;

    /*
    * - 댓글 목록 조회요청 : /api/v1/replies - get
    * - 댓글 개별 조회요청 : /api/v1/replies/72 - get @PathVariable
      - 댓글 쓰기 요청 : /api/v1/replies - post
    * 댓글 수정 요청 : /api/v1/replies - put
    * 댓글 삭제 조회요청 : /api/v1/replies - delete
    * */

    // 댓글 목록 요청
    @GetMapping("")
    public Map<String, Object> list(long boardNo , Page page){
        log.info("boardNo {} , page {}" ,boardNo,page);
        Map<String, Object> replies = replyService.getList(boardNo,page); // 글번호와 페이지 넘버로 가져와야 됨
         return replies;
    }

    // 댓글 등록 요청
    @PostMapping("")
    // 폼으로 요청을 못받음 , json 으로 받아야 되서 @RequestBody
    public String create(@RequestBody Reply reply){ // 이거 잘 됨
        log.info("/api/v1/replies Post! {}" , reply);
        boolean flag = replyService.write(reply);
        return flag ?  "insert-success" : "insert-false";
    }

    // 댓글 수정 요청
    @PutMapping("/{rno}")
    // 폼으로 요청을 못받음 , json 으로 받아야 되서 @RequestBody
    public String modify(@PathVariable long rno , @RequestBody Reply reply){
        reply.setReplyNo(rno); // 리플라이 안에 아무것도 없음
        log.info("/api/v1/replies Put! rno {} reply {}" , rno , reply);
        boolean flag = replyService.modify(reply);
        return flag ?  "mod-success" : "mod-false";
    }

    // 댓글 삭제 요청
    @DeleteMapping("/{rno}")
    public String modify(@PathVariable long rno ){
        log.info("/api/v1/replies delete! rno {}" , rno );
        boolean remove = replyService.remove(rno);
        return  remove ? "del-success" : "del-false";
    }



















}
