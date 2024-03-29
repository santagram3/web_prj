package com.project.web_prj.board.service;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.board.repository.BoardMapper;
import com.project.web_prj.common.search.Search;
import com.project.web_prj.reply.repository.ReplyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    //  private final BoardMapper mapper; 이거 이름 바꿀띠
//    shift + F6 누르고 바꾸면 된다
    private final ReplyMapper replyMapper;
//    private final BoardRepository repository;

    //    // 게시물 등록 요청 중간 처리
//    public boolean saveService(Board board) {
//        log.info("save service start - {}", board);
//        return repository.save(board);
//    }
    // 게시물 등록 요청 중간 처리
    @Transactional // 의미를 잘 모르겟다
    public boolean saveService(Board board) {
        log.info("save service start - {}", board);
        boolean flag = boardMapper.save(board);
        List<String> fileNames = board.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            for (String fileName : fileNames) {
                boardMapper.addFile(fileName);
            }
        }
        return flag;
    }

//    // 게시물 전체 조회 요청 중간 처리
//    public List<Board> findAllService() {
//        log.info("findAll service start");
//        List<Board> boardList = repository.findAll();
//
//        // 목록 중간 데이터처리
//        processConverting(boardList);
//
//        return boardList;
//    }

//    // 게시물 전체 조회 요청 중간 처리
//    public List<Board> findAllService() {
//        log.info("findAll service start");
//        List<Board> boardList = repository.findAll();
//
//        // 목록 중간 데이터처리
//        processConverting(boardList);
//
//        return boardList;
//    }

//    // 게시물 전체 조회 요청 중간 처리 with paging
//    public Map<String, Object> findAllService(Page page) {
//        log.info("findAll service start");
//
//        Map<String, Object> findDataMap = new HashMap<>();
//
//        List<Board> boardList = repository.findAll(page);
//        // 목록 중간 데이터처리
//        processConverting(boardList);
//
//        findDataMap.put("bList", boardList);
//        findDataMap.put("tc", repository.getTotalCount());
//
//        return findDataMap;
//    }

//    // 게시물 전체 조회 요청 중간 처리 with paging
//    public Map<String, Object> findAllService(Page page) {
//        log.info("findAll service start");
//
//        Map<String, Object> findDataMap = new HashMap<>();
//
//        List<Board> boardList = mapper.findAll(page);
//        // 목록 중간 데이터처리
//        processConverting(boardList);
//
//        findDataMap.put("bList", boardList);
//        findDataMap.put("tc", mapper.getTotalCount());
//
//        return findDataMap;
//    }

    // 게시물 전체 조회 요청 중간 처리 with paging
    public Map<String, Object> findAllService(Search search) {
        log.info("findAll service start");

        Map<String, Object> findDataMap = new HashMap<>();

        List<Board> boardList = boardMapper.findAll(search);
        // 목록 중간 데이터처리
        processConverting(boardList);

        findDataMap.put("bList", boardList);
        findDataMap.put("tc", boardMapper.getTotalCount(search));

        return findDataMap;
    }


    private void processConverting(List<Board> boardList) {
        for (Board b : boardList) {
            convertDateFormat(b);
            substringTitle(b);
            checkNewArticle(b);
            setReplyCount(b);


        }
    }

    private void setReplyCount(Board b) {
        b.setReplyCount(replyMapper.getReplyCount(b.getBoardNo()));
    }


    // 신규 게시물 여부 처리
    private void checkNewArticle(Board b) {

        // 게시물의 작성일자와 현재 시간을 대조

        // 게시물의 작성일자 가져오기 - 16억 5초
        long regDateTime = b.getRegDate().getTime();

        // 현재 시간 얻기 (밀리초) - 16억 10초
        long nowTime = System.currentTimeMillis();

        // 현재시간 - 작성시간
        long diff = nowTime - regDateTime;

        // 신규 게시물 제한시간
        long limitTime = 60 * 5 * 1000;

        if (diff < limitTime) {
            b.setNewArticle(true);
        }

    }

    // 이상하게 나오는거 보기좋게 바꾸는 메서드
    private void convertDateFormat(Board b) {
        Date date = b.getRegDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd a hh:mm");
        b.setPrettierDate(sdf.format(date));
    }

    private void substringTitle(Board b) {

        // 만약에 글제목이 5글자 이상이라면
        // 5글자만 보여주고 나머지는 ...처리
        String title = b.getTitle();
        if (title.length() > 5) {
            String subStr = title.substring(0, 5);
            b.setShortTitle(subStr + "...");
        } else {
            b.setShortTitle(title);
        }
    }

    //    // 게시물 상세 조회 요청 중간 처리
//    @Transactional
//    public Board findOneService(Long boardNo, HttpServletResponse response, HttpServletRequest request) {
//        log.info("findOne service start - {}", boardNo);
//        Board board = repository.findOne(boardNo);
//
//        // 해당 게시물 번호에 해당하는 쿠키가 있는지 확인
//        // 쿠키가 없으면 조회수를 상승시켜주고 쿠키를 만들어서 클라이언트에 전송
//        makeViewCount(boardNo, response, request);
//
//        return board;
//    }
    // 게시물 상세 조회 요청 중간 처리
    @Transactional
    public Board findOneService(Long boardNo, HttpServletResponse response, HttpServletRequest request) {
        log.info("findOne service start - {}", boardNo);
        Board board = boardMapper.findOne(boardNo);

        // 해당 게시물 번호에 해당하는 쿠키가 있는지 확인
        // 쿠키가 없으면 조회수를 상승시켜주고 쿠키를 만들어서 클라이언트에 전송
        makeViewCount(boardNo, response, request);

        return board;
    }


//    private void makeViewCount(Long boardNo, HttpServletResponse response, HttpServletRequest request) {
//        // 쿠키를 조회 - 해당 이름의 쿠키가 있으면 쿠키가 들어오고 없으면 null이 들어옴
//        Cookie foundCookie = WebUtils.getCookie(request, "b" + boardNo);
//
//        if (foundCookie == null) {
//            repository.upViewCount(boardNo);
//
//            Cookie cookie = new Cookie("b" + boardNo, String.valueOf(boardNo));// 쿠키 생성
//            cookie.setMaxAge(60); // 쿠키 수명 설정
//            cookie.setPath("/board/content"); // 쿠키 작동 범위
//
//            response.addCookie(cookie); // 클라이언트에 쿠키 전송
//        }
//    }

    private void makeViewCount(Long boardNo, HttpServletResponse response, HttpServletRequest request) {
        // 쿠키를 조회 - 해당 이름의 쿠키가 있으면 쿠키가 들어오고 없으면 null이 들어옴
        // 검증하는 메서드
//        "b" + boardNo  이런 이름의 쿠키가 있는지 확인하는 메서드
        Cookie foundCookie = WebUtils.getCookie(request, "b" + boardNo);

        // 널이다 ? 그럼 새로 하나 줘야지
        if (foundCookie == null) {
            boardMapper.upViewCount(boardNo);

            Cookie cookie = new Cookie("b" + boardNo, String.valueOf(boardNo));// 쿠키 생성
            cookie.setMaxAge(60); // 쿠키 수명 설정
            cookie.setPath("/board/content"); // 쿠키 작동 범위

            response.addCookie(cookie); // 클라이언트에 쿠키 전송
        }
    }

//    private void makeViewCount(Long boardNo) {
//            mapper.upViewCount(boardNo);
//        }


    // 게시물 삭제 요청 중간 처리
//    public boolean removeService(Long boardNo) {
//        log.info("remove service start - {}", boardNo);
//        return repository.remove(boardNo);
//    }

    // 게시물 삭제 요청 중간 처리
    @Transactional
    public boolean removeService(Long boardNo) {
        log.info("remove service start - {}", boardNo);

        //댓글 먼저 모두 삭제
        replyMapper.removeALl(boardNo);


        return boardMapper.remove(boardNo);
    }


//    // 게시물 수정 요청 중간 처리
//    public boolean modifyService(Board board) {
//        log.info("modify service start - {}", board);
//        return repository.modify(board);
//    }


    // 게시물 수정 요청 중간 처리
    public boolean modifyService(Board board) {
        log.info("modify service start - {}", board);
        return boardMapper.modify(board);
    }

    // 첨부파일 목록 가져오는 중간처리
    public List<String> getFilesService(long bno){
        return boardMapper.findFileNames(bno);
    }


}
