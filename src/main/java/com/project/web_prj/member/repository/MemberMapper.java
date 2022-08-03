package com.project.web_prj.member.repository;

import com.project.web_prj.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MemberMapper {

    // 회원 가입 기능
    boolean register(Member member);

    // 중복 체크 기능
    // 체크타입 : 계정, or 이메일
    // 체크값 : 중복검사대상 값
    int isDuplicate(Map<String ,Object> checkMap);
    // 타입, 타입에따른 값

    // 회원 정보 조회 기능
    Member findUser(String account);



}
