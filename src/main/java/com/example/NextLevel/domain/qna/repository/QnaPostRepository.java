// QnaPostRepository.java
package com.example.NextLevel.domain.qna.repository;

import com.example.NextLevel.domain.qna.entity.QnaPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaPostRepository extends JpaRepository<QnaPost,Long> {
    Page<QnaPost> findByTitleContaining(String keyword, Pageable pageable);
}
