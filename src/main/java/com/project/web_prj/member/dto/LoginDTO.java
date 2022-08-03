package com.project.web_prj.member.dto;


import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDTO {
// 로그인할때 따로 !!한다 !!

    // 로그인 할때 클라이언트가 전송하는 데이터
    private String account;
    private String password;
    private boolean autoLogin;
    // 클라이언트에서 넘어오는 정보 이름과 같이 쓴다 !

}
