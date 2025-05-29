package com.example.NextLevel.domain.post.problem.repository;

import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProblemPostRepository extends JpaRepository<ProblemPost, Long> {

    // 각 파라미터가 빈 문자열 "" 이면 Containing 조건이 항상 참이므로, 해당 필드를 사실상 “검색 제외”한 것과 같은 효과를 냅니다.
    Page<ProblemPost> findByTitleContainingIgnoreCaseAndProfessorNameContainingIgnoreCaseAndSubjectContainingIgnoreCaseAndSchoolContainingIgnoreCase(
            String title,
            String professorName,
            String subject,
            String school,
            Pageable pageable
    );

}
