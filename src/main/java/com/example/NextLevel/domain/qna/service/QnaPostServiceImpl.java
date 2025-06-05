// QnaPostServiceImpl.java
package com.example.NextLevel.domain.qna.service.impl;

import com.example.NextLevel.domain.qna.entity.QnaPost;
import com.example.NextLevel.domain.qna.repository.QnaPostRepository;
import com.example.NextLevel.domain.qna.dto.request.QnaPostRequest;
import com.example.NextLevel.domain.qna.dto.response.QnaPostResponse;
import com.example.NextLevel.domain.qna.service.QnaPostService;
import com.example.NextLevel.domain.qna.service.QnaAnswerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaPostServiceImpl implements QnaPostService {
    private final QnaPostRepository repo;
    private final QnaAnswerService answerService;

    @Override
    public Page<QnaPostResponse> list(Pageable pageable) {
        return repo.findAll(pageable)
                   .map(p -> QnaPostResponse.of(p, answerService.listByPost(p.getId())));
    }

    @Override
    public QnaPostResponse get(Long id) {
        QnaPost p = repo.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("글을 찾을 수 없습니다. id=" + id));
        return QnaPostResponse.of(p, answerService.listByPost(id));
    }

    @Override
    public QnaPostResponse create(QnaPostRequest req, String username) {
        QnaPost p = QnaPost.builder()
            .title(req.getTitle())
            .content(req.getContent())
            .author(username)
            .build();
        QnaPost saved = repo.save(p);
        return QnaPostResponse.of(saved, List.of());
    }

    @Override
    public QnaPostResponse update(Long id, QnaPostRequest req, String username) {
        QnaPost p = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("글을 찾을 수 없습니다. id=" + id));
        if (!p.getAuthor().equals(username))
            throw new AccessDeniedException("수정 권한이 없습니다.");
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());
        QnaPost saved = repo.save(p);
        return QnaPostResponse.of(saved, answerService.listByPost(id));
    }

    @Override
    public void delete(Long id, String username) {
        QnaPost p = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("글을 찾을 수 없습니다. id=" + id));
        if (!p.getAuthor().equals(username))
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        repo.delete(p);
    }
}
