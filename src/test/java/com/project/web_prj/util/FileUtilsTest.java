package com.project.web_prj.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileUtilsTest {

    @Test
    @DisplayName("랜덤으로 잘 나오는지 확인")
    public void randomUUIDTest(){

        for (int i = 0; i <20 ; i++) {
            System.out.println(UUID.randomUUID().toString());
        }


    }


}