package com.example.NextLevel.domain.comment.controller;

import com.example.NextLevel.common.response.ApiResponse;
import com.example.NextLevel.domain.comment.dto.request.Request;
import com.example.NextLevel.domain.comment.dto.request.UpdateRequest;
import com.example.NextLevel.domain.comment.dto.response.Response;
import com.example.NextLevel.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem-posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글·대댓글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @PathVariable Long postId,
            @RequestBody Request req,
            Authentication authentication
    ) {
        Long id = commentService.addComment(postId, req, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    // 댓글 + 대댓글 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<Response>>> list(
            @PathVariable Long postId
    ) {
        List<Response> comments = commentService.getComments(postId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody UpdateRequest req,
            Authentication authentication
    ) {
        commentService.updateComment(commentId, req.getContent(), authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        commentService.deleteComment(commentId, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
