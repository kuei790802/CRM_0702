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

        // 把帳號資訊放進 request scope，給 controller 用
        String account = claims.getSubject(); // 或 claims.get("account", String.class)
        req.setAttribute("account", account);
        System.out.println("Set request attribute 'account' = " + account);
        System.out.println("🪪 JWT 中帳號: " + account);

        // 把腳色資訊放進 request scope，給 controller 用(但好像customer沒有(?)
        String role = claims.get("role", String.class);
        req.setAttribute("role", role);

        System.out.println("Token ok, account = " + account);
        System.out.println("JwtAspect checkJwt END");
        return joinPoint.proceed();
    }

}
