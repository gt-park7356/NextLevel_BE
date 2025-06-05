// QnaAnswerRepository.java
package com.example.NextLevel.domain.qna.repository;

import com.example.NextLevel.domain.qna.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QnaAnswerRepository extends JpaRepository<QnaAnswer,Long> {
    List<QnaAnswer> findByPostId(Long postId);
}
