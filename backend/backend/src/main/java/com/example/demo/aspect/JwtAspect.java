package com.example.demo.aspect;

import com.example.demo.security.JwtTool;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class JwtAspect {

    @Around("@annotation(com.example.demo.security.CheckJwt)")
    public Object checkJwt(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("JwtAspect checkJwt START");
        HttpServletRequest req =
                ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader =  req.getHeader("Authorization");
        if (authHeader == null){
            throw new JwtException("Token format error: Missing");

        }
        if (!authHeader.startsWith("Bearer ")){
            throw new JwtException("Token format error: invalid 'Bearer ' prefix");
        }
        String token = authHeader.substring(7);
        Claims claims = JwtTool.parseToken(token);

        if (claims == null) {
            throw new JwtException("Token expired");
        }

        // ✅ 把帳號資訊放進 request scope，給 controller 用
        String account = claims.getSubject();
        req.setAttribute("account", account);
        System.out.println("Set request attribute 'account' = " + account);

        // ★★★【修改重點】★★★
        // 從 claims 中獲取 customerId 並存入 request scope
        Long customerId = claims.get("customerId", Long.class); // 從 JWT claims 取出 customerId
        if (customerId == null) {
            throw new JwtException("Token is missing customerId claim");
        }
        req.setAttribute("customerId", customerId); // 將 customerId 放入 request attribute
        System.out.println("Set request attribute 'customerId' = " + customerId);
        // ★★★【修改結束】★★★


        System.out.println("Token ok, account = " + account);
        System.out.println("JwtAspect checkJwt END");
        return joinPoint.proceed();
    }

}