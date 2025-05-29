package com.example.NextLevel.domain.comment.repository;

import com.example.NextLevel.domain.comment.model.Comment;
import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 최상위 댓글만 조회 (parent IS NULL)
    List<Comment> findByPostAndParentIsNullOrderByCreatedAtAsc(ProblemPost post);
}
