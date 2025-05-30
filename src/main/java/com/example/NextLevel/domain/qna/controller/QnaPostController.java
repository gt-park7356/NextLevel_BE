// QnaPostController.java
package com.example.NextLevel.domain.qna.controller;

import com.example.NextLevel.domain.qna.dto.request.QnaPostRequest;
import com.example.NextLevel.domain.qna.dto.response.QnaPostResponse;
import com.example.NextLevel.domain.qna.service.QnaPostService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/qna-posts")
@RequiredArgsConstructor
@Validated
public class QnaPostController {

    private final QnaPostService svc;

    @GetMapping
    public Page<QnaPostResponse> list(
        @PageableDefault(sort="createdAt", direction=Sort.Direction.DESC)
        Pageable pageable
    ) {
        return svc.list(pageable);
    }

    @GetMapping("/{id}")
    public QnaPostResponse get(@PathVariable Long id) {
        return svc.get(id);
    }

    @PostMapping
    public ResponseEntity<QnaPostResponse> create(
        @Valid @RequestBody QnaPostRequest req,
        Authentication auth
    ) {
        QnaPostResponse res = svc.create(req, auth.getName());
        return ResponseEntity
            .created(URI.create("/api/qna-posts/"+res.getId()))
            .body(res);
    }

    @PutMapping("/{id}")
    public QnaPostResponse update(
        @PathVariable Long id,
        @Valid @RequestBody QnaPostRequest req,
        Authentication auth
    ) {
        return svc.update(id, req, auth.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        Authentication auth
    ) {
        svc.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
