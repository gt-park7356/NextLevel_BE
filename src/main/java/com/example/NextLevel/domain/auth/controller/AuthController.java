package com.example.NextLevel.domain.auth.controller;

import com.example.NextLevel.common.response.ApiResponse;
import com.example.NextLevel.domain.auth.dto.LoginDTO;
import com.example.NextLevel.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        Map<String, String> tokens = authService.login(loginDTO, response);
        return ResponseEntity.ok(ApiResponse.success(tokens));
    }


}
