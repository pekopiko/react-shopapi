package com.react.shopapi.security.handler;

import com.google.gson.Gson;
import com.react.shopapi.dto.MemberDTO;
import com.react.shopapi.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

// 로그인 성공시 실행할 클래스
@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("************************** CustomLoginSuccessHandler");
        // * 로그인 성공 -> JSON 문자열로 응답해줄 데이터 생성 -> 응답 *
        // 응답 데이터 생성 -> 사용자 정보
        MemberDTO memberDTO = (MemberDTO)authentication.getPrincipal();
        Map<String, Object> claims = memberDTO.getClaims(); // 사용자정보 Map타입으로 변환

        // JWT 토큰 생성
        String accessToken = JWTUtil.generateToken(claims, 10); // 10분
        String refreshToken = JWTUtil.generateToken(claims, 60 * 24); // 24시간
        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        // 위 응답 데이터를 JSON문자열로 변환
        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);

        // 응답하기 (응답 메세지를 보내기)
        response.setContentType("application/json; charset=UTF-8"); // 응답데이터 형태 헤더정보추가
        PrintWriter writer = response.getWriter();
        writer.println(jsonStr);
        writer.close();
    }
}
