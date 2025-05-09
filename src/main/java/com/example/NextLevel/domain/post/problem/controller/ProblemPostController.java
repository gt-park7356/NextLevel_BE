package com.example.NextLevel.domain.post.problem.controller;

import com.example.NextLevel.domain.post.problem.dto.request.ProblemPostRequest;
import com.example.NextLevel.domain.post.problem.dto.response.ProblemPostResponse;
import com.example.NextLevel.domain.post.problem.service.ProblemPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem-posts")
public class ProblemPostController {

    private final ProblemPostServiceImpl problemPostServiceImpl;

    // 게시글 생성
    @PostMapping(value = "/create")
    public ResponseEntity<ProblemPostResponse> create(
            @RequestPart(name = "request") ProblemPostRequest request,
            @RequestPart(name = "data") MultipartFile problemData,
            Authentication authentication
    ) {
        String username = authentication.getName();
        ProblemPostResponse savedProblemPostResponse = problemPostServiceImpl.register(request, problemData, username);
        return ResponseEntity.ok(savedProblemPostResponse);
    }

    // 게시글 상세 조회
    @GetMapping("/{problemPostId}")
    public ResponseEntity<ProblemPostResponse> read(@PathVariable("problemPostId") Long postId){
        return ResponseEntity.ok(problemPostServiceImpl.read(postId));
    }
}
