package com.project.web_prj.board.controller;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    public String list(Model model) {
        log.info("controller request /board/list GET!");
        List<Board> boardList = boardService.findAllService();
        log.debug("return data - {}", boardList);

        model.addAttribute("bList", boardList);
        return "board/board-list";
    }

    // 게시물 상세 조회 요청
    @GetMapping("/content/{boardNo}")
    public String content(@PathVariable Long boardNo, Model model, HttpServletResponse response, HttpServletRequest request) {
        log.info("controller request /board/content GET! - {}", boardNo);
        Board board = boardService.findOneService(boardNo, response, request);
        log.info("return data - {}", board);
        model.addAttribute("b", board);
        return "board/board-detail";
    }

    // 게시물 쓰기 화면 요청
    @GetMapping("/write")
    public String write() {
        log.info("controller request /board/write GET!");
        return "board/board-write";
    }

    // 게시물 등록 요청
    @PostMapping("/write")
    public String write(Board board) {
        log.info("controller request /board/write POST! - {}", board);
        boolean flag = boardService.saveService(board);
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

}
