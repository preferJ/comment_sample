package com.its.board.controller;

import com.its.board.HomeController;
import com.its.board.dto.BoardDTO;
import com.its.board.dto.MemberDTO;
import com.its.board.service.BoardService;
import com.its.board.service.MemberService;
import com.mysql.cj.xdevapi.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member")

public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardService boardService;

    @Autowired
    private HomeController homeController;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/save";
    }

    @PostMapping("/saveReq")
    public String saveReq(@ModelAttribute MemberDTO memberDTO, Model model) {
        memberService.saveReq(memberDTO);
        model.addAttribute("memberId", memberDTO.getMemberId());
        return "memberPages/login";
    }

    @GetMapping("/login")
    public String login() {
        return "memberPages/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.removeAttribute("memberId");
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "index";
    }

    @PostMapping("/loginReq")
    public @ResponseBody MemberDTO loginReq(@RequestParam String memberId, HttpSession session) {
        MemberDTO memberDTO = memberService.findById(memberId);
        System.out.println(memberDTO);
        session.setAttribute("memberId", memberDTO.getMemberId());
        return memberDTO;
    }

    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {
        MemberDTO memberDTO = memberService.findById((String) (session.getAttribute("memberId")));
        model.addAttribute("memberDTO", memberDTO);
        return "memberPages/myPage";
    }

    @PostMapping("/idDupCheck")
    public @ResponseBody boolean idDupCheck(@RequestParam String memberId) {
        System.out.println("실행");
        MemberDTO memberDTO = memberService.findById(memberId);
        System.out.println(memberDTO);
        System.out.println(memberDTO==null);
        if (memberDTO == null) {
            return true; // 아이디 사용 가능
        } else
            return false; // 아이디 사용 불가
    }


}
