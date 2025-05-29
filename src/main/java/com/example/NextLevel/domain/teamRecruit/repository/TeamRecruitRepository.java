package com.example.NextLevel.domain.teamRecruit.repository;

import com.example.NextLevel.domain.teamRecruit.entity.TeamRecruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRecruitRepository extends JpaRepository<TeamRecruit, Long> {

    Page<TeamRecruit> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
        String titleKeyword,
        String contentKeyword,
        Pageable pageable
    );
}
