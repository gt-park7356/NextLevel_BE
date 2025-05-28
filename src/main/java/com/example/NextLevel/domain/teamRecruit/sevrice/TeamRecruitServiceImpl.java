package com.example.NextLevel.domain.teamRecruit.service;

import com.example.NextLevel.domain.teamRecruit.dto.request.TeamRecruitRequest;
import com.example.NextLevel.domain.teamRecruit.dto.response.TeamRecruitResponse;
import com.example.NextLevel.domain.teamRecruit.entity.TeamRecruit;
import com.example.NextLevel.domain.teamRecruit.repository.TeamRecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class TeamRecruitServiceImpl implements TeamRecruitService {
    private final TeamRecruitRepository repo;

    @Override
    public TeamRecruitResponse create(TeamRecruitRequest req, String username) {
        TeamRecruit tr = repo.save(TeamRecruit.builder()
            .title(req.getTitle())
            .content(req.getContent())
            .author(username)
            .build()
        );
        return TeamRecruitResponse.of(tr);
    }

    @Override
    public Page<TeamRecruitResponse> list(Pageable pageable, String keyword) {
        Page<TeamRecruit> page;
        if (StringUtils.hasText(keyword)) {
            page = repo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                keyword, keyword, pageable
            );
        } else {
            page = repo.findAll(pageable);
        }
        return page.map(TeamRecruitResponse::of);
    }

    @Override
    public TeamRecruitResponse get(Long id) {
        TeamRecruit tr = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + id));
        return TeamRecruitResponse.of(tr);
    }

    @Override
    public TeamRecruitResponse update(Long id, TeamRecruitRequest req, String username) {
        TeamRecruit tr = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + id));
        if (!tr.getAuthor().equals(username)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        tr.setTitle(req.getTitle());
        tr.setContent(req.getContent());
        return TeamRecruitResponse.of(repo.save(tr));
    }

    @Override
    public void delete(Long id, String username) {
        TeamRecruit tr = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. id=" + id));
        if (!tr.getAuthor().equals(username)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        repo.delete(tr);
    }
}
