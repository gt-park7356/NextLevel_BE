package com.example.NextLevel.domain.post.problem.repository;

import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProblemPostRepository extends JpaRepository<ProblemPost, Long> {

}
