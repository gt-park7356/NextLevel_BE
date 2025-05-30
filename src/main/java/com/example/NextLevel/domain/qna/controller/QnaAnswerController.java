// QnaAnswerController.java
package com.example.NextLevel.domain.qna.controller;

import com.example.NextLevel.domain.qna.dto.request.QnaAnswerRequest;
import com.example.NextLevel.domain.qna.dto.response.QnaAnswerResponse;
import com.example.NextLevel.domain.qna.service.QnaAnswerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/qna-posts/{postId}/answers")
@RequiredArgsConstructor
@Validated
public class QnaAnswerController {

    private final QnaAnswerService svc;

    @PostMapping
    public ResponseEntity<QnaAnswerResponse> create(
        @PathVariable Long postId,
        @Valid @RequestBody QnaAnswerRequest req,
        Authentication auth
    ) {
        QnaAnswerResponse res = svc.create(postId, req, auth.getName());
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public List<QnaAnswerResponse> list(@PathVariable Long postId) {
        return svc.listByPost(postId);
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<QnaAnswerResponse> update(
        @PathVariable Long answerId,
        @Valid @RequestBody QnaAnswerRequest req,
        Authentication auth
    ) {
        QnaAnswerResponse res = svc.update(answerId, req, auth.getName());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long answerId,
        Authentication auth
    ) {
        svc.delete(answerId, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
