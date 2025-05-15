package com.example.NextLevel.domain.post.problem.controller;

import com.example.NextLevel.common.response.ApiResponse;
import com.example.NextLevel.domain.post.problem.dto.request.ProblemPostRequest;
import com.example.NextLevel.domain.post.problem.dto.response.ProblemPostResponse;
import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import com.example.NextLevel.domain.post.problem.service.ProblemPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ApiResponse> create(
            @RequestPart(name = "request") ProblemPostRequest request,
            @RequestPart(name = "data") MultipartFile problemData,
            Authentication authentication
    ) {
        String username = authentication.getName();
        ProblemPostResponse savedProblemPostResponse = problemPostServiceImpl.register(request, problemData, username);
        return ResponseEntity.ok(ApiResponse.success(savedProblemPostResponse));
    }

    // 게시글 상세 조회
    @GetMapping("/{problemPostId}")
    public ResponseEntity<ApiResponse> read(@PathVariable("problemPostId") Long postId){
        ProblemPostResponse post = problemPostServiceImpl.read(postId);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @GetMapping ("/all") // 게시글 전체조회
    public ResponseEntity<ApiResponse> getAllPosts(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<ProblemPostResponse> posts = problemPostServiceImpl.getAllPosts(pageable);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/search")  // 제목, 전공, 과목, 학교로 게시글 검색
    public ResponseEntity<ApiResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String professorName,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String school,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable
    ) {
        Page<ProblemPostResponse> results = problemPostServiceImpl.search(title, professorName, subject, school, pageable);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    // 게시글 수정
    @PatchMapping(
            value = "/{problemPostId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long problemPostId,
            @RequestPart("request") ProblemPostRequest request,
            @RequestPart(name = "data", required = false) MultipartFile problemData,
            Authentication authentication
    ) {
        String username = authentication.getName();
        ProblemPostResponse updated = problemPostServiceImpl.update(problemPostId, request, problemData, username);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    //게시글 삭제
    @DeleteMapping("/{problemPostId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("problemPostId") Long postId, Authentication authentication){
        String username = authentication.getName();
        problemPostServiceImpl.delete(postId, username);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
