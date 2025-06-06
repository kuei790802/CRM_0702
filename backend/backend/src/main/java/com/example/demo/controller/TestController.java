package com.example.demo.controller;

import com.example.demo.security.CheckJwt;
import com.example.demo.utils.JwtTool;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tset")
public class TestController {

    @RequestMapping("/m1")
    public void m1(@RequestParam String email){
        String token = JwtTool.createToken(email);
        System.out.println(token);
    }

    @RequestMapping("/m2/{token}")
    public void m2(@PathVariable String token){
        String email = JwtTool.parseToken(token);
        System.out.println(email);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email){
//        驗證基本帳號密碼，過了才給token

        return JwtTool.createToken(email);
    }

    @CheckJwt
    @GetMapping("/member/m1")
    public String getMemberData1(){
        return "重要資料1";
    }

    @CheckJwt
    @GetMapping("/member/m2")
    public String getMemberData2(){
        return "重要資料2";
    }

}
