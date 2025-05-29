package com.example.NextLevel.domain.teamRecruit.controller;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import com.example.NextLevel.domain.teamRecruit.dto.request.TeamRecruitRequest;
import com.example.NextLevel.domain.teamRecruit.dto.response.TeamRecruitResponse;
import com.example.NextLevel.domain.teamRecruit.service.TeamRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/team-recruits")
@RequiredArgsConstructor
@Validated
public class TeamRecruitController {

    private final TeamRecruitService svc;

    /** 1) 목록 조회 (페이징 + 검색) */
    @GetMapping
    public Page<TeamRecruitResponse> list(
        @RequestParam(value = "keyword", required = false) String keyword,
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return svc.list(pageable, keyword);
    }

    /** 2) 단건 조회 */
    @GetMapping("/{id}")
    public TeamRecruitResponse get(@PathVariable Long id) {
        return svc.get(id);
    }

    /** 3) 생성 (인증 필요) */
    @PostMapping
    public ResponseEntity<TeamRecruitResponse> create(
        @Valid @RequestBody TeamRecruitRequest req,
        Authentication auth
    ) {
        // auth.getName() 은 JWT 에 담긴 username
        TeamRecruitResponse res = svc.create(req, auth.getName());
        return ResponseEntity
            .created(URI.create("/api/team-recruits/" + res.getId()))
            .body(res);
    }

    /** 4) 수정 (인증 + 작성자 검사) */
    @PutMapping("/{id}")
    public TeamRecruitResponse update(
        @PathVariable Long id,
        @Valid @RequestBody TeamRecruitRequest req,
        Authentication auth
    ) {
        return svc.update(id, req, auth.getName());
    }

    /** 5) 삭제 (인증 + 작성자 검사) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        Authentication auth
    ) {
        svc.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
