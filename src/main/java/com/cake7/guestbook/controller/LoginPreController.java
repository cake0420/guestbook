//package com.cake7.guestbook.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/oauth2/code")
//public class LoginPreController {
//
//    @GetMapping("/google")
//    public String googleLogin(Authentication authentication) {
//        System.out.println("authentication : " + authentication.isAuthenticated());
//        if (authentication != null && authentication.isAuthenticated()) {
//            return "redirect:/";
//        }
//        return "redirect:/login/oauth2/code/google";
//    }
//
//    @GetMapping("/naver")
//    public String naverLogin(Authentication authentication) {
//        System.out.println("authentication : " + authentication.isAuthenticated());
//        if (authentication != null && authentication.isAuthenticated()) {
//            return "redirect:/";
//        }
//        return "redirect:/login/oauth2/code/naver";
//    }
//}
