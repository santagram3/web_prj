package com.project.web_prj.board.repository;

import com.project.web_prj.board.domain.Board;
import com.project.web_prj.common.paging.Page;
import com.project.web_prj.common.search.Search;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardMapperTest {

    @Autowired
    BoardMapper mapper;

    @Test
    @DisplayName("저장 잘 되는지 잘 보자고 // ")
    void saveTest() {
        for (int i = 1; i <320 ; i++) {
        Board board = new Board();
            board.setWriter("삼삼이"+i);
            board.setTitle("인생사"+i);
            board.setContent("고독하구만"+i);

            mapper.save(board);
        }

    }

    @Test
    @DisplayName("모두 불러 모으기 ~ ")
    void findAllTest() {
        List<Board> all = mapper.findAll(new Search("title","1"));
        for (Board b : all) {
            System.out.println(b);

        }
    }

    @Test
    @DisplayName("번호가1L인 애의 이름은 삼삼이0")
    void findOneTest() {
        Board one = mapper.findOne(1L);
        assertEquals("삼삼이0",one.getWriter());
    }

    @Test
    void removeTest() {
        boolean flag = mapper.remove(1L);
        assertTrue(flag);

    }

    @Test
    void modify() {
//        mapper.modify()
    }

    @Test
    void getTotalCount() {
    }

    @Test
    void upViewCount() {
    }




    @Test
    @DisplayName("특정 게시물에 첨부된 파일경로들을 조회한다.")
    void findFileNamesTest() {
        //given
        Long bno = 323L;

        //when
        List<String> fileNames = mapper.findFileNames(bno);

        //then
        fileNames.forEach(System.out::println);
        assertEquals(2, fileNames.size());

    }












}