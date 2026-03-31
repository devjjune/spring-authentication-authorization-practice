package com.back.domain.member.controller;

import com.back.domain.member.dto.MemberDto;
import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.global.exception.ServiceException;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;

    // 회원가입 요청 시 클라이언트가 보내는 JSON 데이터를 담는 DTO
    record MemberJoinReqBody(
            String username,
            String password,
            String nickname
    ) {
    }

    // 회원가입 처리 후 생성된 회원 정보를 응답으로 전달하기 위한 DTO
    record MemberJoinResBody(
            MemberDto memberDto
    ) {
    }

    @PostMapping("/join")
    public RsData<MemberDto> join(@RequestBody @Valid MemberJoinReqBody reqBody) {

        Member member = memberService.join(reqBody.username, reqBody.password, reqBody.nickname);

        return new RsData(
                "회원가입이 완료되었습니다. %s님 환영합니다.".formatted(member.getName()),
                "201-1",
                new MemberJoinResBody(
                        new MemberDto(member)
                )
        );
    }

    // 로그인 요청 시 클라이언트가 보내는 인증 정보(username, password)를 담는 DTO
    record MemberLoginReqBody(
            String username,
            String password
    ) {
    }

    // 로그인 성공 시 발급된 인증 정보(apiKey)를 응답으로 전달하기 위한 DTO
    record MemberLoginResBody(
            String apiKey
    ) {
    }

    @PostMapping("/login")
    public RsData<MemberLoginResBody> login(@RequestBody @Valid MemberLoginReqBody reqBody) {

        Member actor = memberService.findByUsername(reqBody.username).orElseThrow(
                () -> new ServiceException("401-1", "존재하지 않는 아이디입니다.")
        );

        if(!actor.getPassword().equals(reqBody.password)){
            throw new ServiceException("401-2", "비밀번호가 일치하지 않습니다.");
        }

        return new RsData(
                "%s님 환영합니다.".formatted(actor.getName()),
                "200-1",
                new MemberLoginResBody(actor.getApiKey())
        );
    }
}
