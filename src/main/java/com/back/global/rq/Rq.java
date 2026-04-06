package com.back.global.rq;

import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.global.exception.ServiceException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Rq {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final MemberService memberService;

    public Member getActor() {

        String authorizationHeader = getHeader("Authorization", "");

        String apiKey = "";
        String accessToken = "";

        // 1. 헤더 방식
        if (!authorizationHeader.isBlank()) {

            if (!authorizationHeader.startsWith("Bearer ")) {
                throw new ServiceException("401-2", "잘못된 형식의 인증데이터입니다.");
            }

            String[] bits = authorizationHeader.split(" ", 3);

            if (bits.length < 2) {
                throw new ServiceException("401-2", "잘못된 형식의 인증데이터입니다.");
            }

            apiKey = bits[1];
            accessToken = bits.length == 3 ? bits[2] : "";
        }

        // 2. 쿠키 방식
        else {
            apiKey = getCookieValue("apiKey", "");
            accessToken = getCookieValue("accessToken", "");
        }

        Member member = null;

        // 3. accessToken 기반 인증 (우선)
        if (!accessToken.isBlank()) {
            Map<String, Object> payload = memberService.payloadOrNull(accessToken);

            if (payload != null) {
                int id = (int) payload.get("id");

                String name = (String) payload.get("name");
                member = new Member(id, name);
            }
        }

        // 4. apiKey 기반 인증 (fallback)
        if (member == null && !apiKey.isBlank()) {
            member = memberService.findByApiKey(apiKey)
                    .orElseThrow(() ->
                            new ServiceException("401-4", "API 키가 유효하지 않습니다.")
                    );
        }

        // 5. 둘 다 실패
        if (member == null) {
            throw new ServiceException("401-1", "인증 정보가 존재하지 않습니다.");
        }

        return member;
    }

    private String getHeader(String name, String defaultValue) {
        return Optional
                .ofNullable(request.getHeader(name))
                .filter(headerValue -> !headerValue.isBlank())
                .orElse(defaultValue);
    }

    private String getCookieValue(String name, String defaultValue) {
        return Optional
                .ofNullable(request.getCookies())
                .flatMap(
                        cookies ->
                                Arrays.stream(cookies)
                                        .filter(cookie -> cookie.getName().equals(name))
                                        .map(Cookie::getValue)
                                        .filter(value -> !value.isBlank())
                                        .findFirst()
                )
                .orElse(defaultValue);
    }

    public void deleteCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    public void addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");

        response.addCookie(cookie);
    }
}
