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

        System.out.println("Token ok");
        return joinPoint.proceed();
    }

}
