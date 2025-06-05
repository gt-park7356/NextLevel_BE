// QnaAnswerServiceImpl.java
package com.example.NextLevel.domain.qna.service.impl;

import com.example.NextLevel.domain.qna.entity.QnaAnswer;
import com.example.NextLevel.domain.qna.entity.QnaPost;
import com.example.NextLevel.domain.qna.repository.QnaAnswerRepository;
import com.example.NextLevel.domain.qna.repository.QnaPostRepository;
import com.example.NextLevel.domain.qna.dto.request.QnaAnswerRequest;
import com.example.NextLevel.domain.qna.dto.response.QnaAnswerResponse;
import com.example.NextLevel.domain.qna.service.QnaAnswerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaAnswerServiceImpl implements QnaAnswerService {
    private final QnaAnswerRepository answerRepo;
    private final QnaPostRepository postRepo;

    @Override
    public QnaAnswerResponse create(Long postId, QnaAnswerRequest req, String username) {
        QnaPost post = postRepo.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("글을 찾을 수 없습니다. id=" + postId));
        QnaAnswer a = QnaAnswer.builder()
            .content(req.getContent())
            .author(username)
            .post(post)
            .build();
        return QnaAnswerResponse.of(answerRepo.save(a));
    }

    @Override
    public List<QnaAnswerResponse> listByPost(Long postId) {
        return answerRepo.findByPostId(postId).stream()
                         .map(QnaAnswerResponse::of)
                         .collect(Collectors.toList());
    }

    @Override
    public QnaAnswerResponse update(Long id, QnaAnswerRequest req, String username) {
        QnaAnswer a = answerRepo.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("답변을 찾을 수 없습니다. id=" + id));
        if (!a.getAuthor().equals(username))
            throw new AccessDeniedException("수정 권한이 없습니다.");
        a.setContent(req.getContent());
        return QnaAnswerResponse.of(answerRepo.save(a));
    }

    @Override
    public void delete(Long id, String username) {
        QnaAnswer a = answerRepo.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("답변을 찾을 수 없습니다. id=" + id));
        if (!a.getAuthor().equals(username))
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        answerRepo.delete(a);
    }
}
