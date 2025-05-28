package com.example.NextLevel.domain.teamRecruit.service;

import com.example.NextLevel.domain.teamRecruit.dto.request.TeamRecruitRequest;
import com.example.NextLevel.domain.teamRecruit.dto.response.TeamRecruitResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRecruitService {
    TeamRecruitResponse create(TeamRecruitRequest req, String username);
    Page<TeamRecruitResponse> list(Pageable pageable, String keyword);
    TeamRecruitResponse get(Long id);
    TeamRecruitResponse update(Long id, TeamRecruitRequest req, String username);
    void delete(Long id, String username);
}
