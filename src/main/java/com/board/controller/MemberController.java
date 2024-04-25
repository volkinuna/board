package com.board.controller;

import com.board.dto.MemberFormDto;
import com.board.entity.Member;
import com.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    //로그인 화면
    @GetMapping(value = "members/login")
    public String loginMember() {
        return "member/loginForm";
    }

    //회원가입 화면
    @GetMapping(value = "/members/new")
    public String registerForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/registerForm";
    }

    //회원가입 처리
    @PostMapping(value = "/members/new")
    public String registerForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) return "member/registerForm";

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/loginForm";
        }

        return "redirect:/";
    }

    //로그인 실패시
    @GetMapping(value = "/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요.");
        return "member/loginForm";
    }
}
