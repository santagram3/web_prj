package com.project.web_prj.member.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    private String account ;
    private String password;
    private String name;
    private String email;
    private Auth auth;
    private Date regDate;

}
