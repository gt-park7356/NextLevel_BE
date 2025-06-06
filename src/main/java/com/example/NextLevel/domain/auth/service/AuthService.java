package com.example.NextLevel.domain.auth.service;

import com.example.NextLevel.common.exception.CustomException;
import com.example.NextLevel.common.exception.ErrorCode;
import com.example.NextLevel.domain.auth.dto.LoginDTO;
import com.example.NextLevel.domain.auth.util.JWTUtil;
import com.example.NextLevel.domain.member.dto.MemberDTO;
import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    private static final int ACCESS_TOKEN_EXPIRATION = 600; // 2시간
    private static final int REFRESH_TOKEN_EXPIRATION = 60 * 24 * 7; // 7일

    public Map<String, String> login(LoginDTO loginDTO, HttpServletResponse response) {
        // 1. 사용자 검증
        Member member = memberRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(loginDTO.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCHED_PASSWORD);
        }

        // 2. 사용자 정보 가져오기
        MemberDTO foundMemberDTO = new MemberDTO(member);

        // 3. 토큰 생성
        Map<String, Object> payloadMap = foundMemberDTO.getPayload();
        String accessToken = jwtUtil.createToken(payloadMap, ACCESS_TOKEN_EXPIRATION);
        String refreshToken = jwtUtil.createToken(Map.of("username", foundMemberDTO.getUsername()), REFRESH_TOKEN_EXPIRATION);

        log.info("--- accessToken : {}", accessToken);
        log.info("--- refreshToken : {}", refreshToken);

        // 5. 응답 데이터 반환
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }


}
