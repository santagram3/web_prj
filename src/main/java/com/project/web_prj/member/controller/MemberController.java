package com.project.web_prj.member.controller;


import com.project.web_prj.member.domain.Member;
import com.project.web_prj.member.dto.LoginDTO;
import com.project.web_prj.member.service.LoginFlag;
import com.project.web_prj.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Log4j2
public class MemberController {

    private final MemberService memberService;

    // 회원가입 양식 띄우기
    @GetMapping("/sign-up")
    public void signUp() {
        log.info("well come!");
    }

    // 실질적 회원원가입 요청
    @PostMapping("/sign-up")
    public String signUp(Member member, RedirectAttributes ra) {
        log.info("/member/sign-up Post ! {}", member);
        boolean flag = memberService.signUp(member);
        ra.addFlashAttribute("msg", "reg-success");
        return flag ? "redirect:/member/sign-in" : "redirect:/member/sign-up";
    }

    // 아이디 이메일 중목확인 비동기 요청 처리
    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<Boolean> check(String type, String value) {
        log.info("member/check type : {}  value :{}", type, value);
        boolean flag = memberService.checkSignUpValue(type, value);
        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

    // 로그인 화면을 열어주는 요청 처리 !
    @GetMapping("/sign-in")
    public void signIn(HttpServletRequest request) {
        log.info("로그인 화면 켜짐");

        // 요청 정보 헤더 안에는 Referer 라는 키가 있는데
        // 여기안에는 이 페이지로 진입 할때 어디에서 왔는지 URI 정보가 들어있음
        String referer = request.getHeader("Referer");
        log.info("refer : {}" ,referer);

        request.getSession().setAttribute("redirectURI",referer);

        log.info("welcom login page");
    }

    // 로그인 요청
    @PostMapping("sign-in")
    public String signIn(LoginDTO inputData, Model model, HttpSession session) {
        // member 로 받지 않고 , LoginDTO 로 받는 이유
        // 다 매칭될수있도록 만들어야 된다 !! // 로그보고 확인한다 !!
//        HttpSession session// 세선정보 객체
        log.info("/member/sign-in Post {}", inputData);
//        log.info("session timeOut {}" ,session.getMaxInactiveInterval());

        LoginFlag flag = memberService.logIn(inputData, session);
        if (flag == LoginFlag.SUCCESS) {
            log.info("Login Success");
            String redirectURI = (String) session.getAttribute("redirectURI");
            return "redirect:"+ redirectURI;
        }
        model.addAttribute("loginMsg", flag);
        return "member/sign-in";
    }


    // 로그아웃
    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        if (session.getAttribute("loginUser") != null) {
            // 1. 세션에서 정보를 삭제한다
            session.removeAttribute("loginUser");
            // 2. 세션 을 무효화 한다 !
            session.invalidate();
            return "redirect:/";
        }
        return "redirect:/member/sign-in";
    }

}
