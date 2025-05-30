// QnaAnswerService.java
package com.example.NextLevel.domain.qna.service;

import com.example.NextLevel.domain.qna.dto.request.QnaAnswerRequest;
import com.example.NextLevel.domain.qna.dto.response.QnaAnswerResponse;
import java.util.List;

public interface QnaAnswerService {
    QnaAnswerResponse create(Long postId, QnaAnswerRequest req, String username);
    List<QnaAnswerResponse> listByPost(Long postId);
    QnaAnswerResponse update(Long id, QnaAnswerRequest req, String username);
    void delete(Long id, String username);
}
