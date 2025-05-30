// QnaPostService.java
package com.example.NextLevel.domain.qna.service;

import com.example.NextLevel.domain.qna.dto.request.QnaPostRequest;
import com.example.NextLevel.domain.qna.dto.response.QnaPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaPostService {
    Page<QnaPostResponse> list(Pageable pageable);
    QnaPostResponse get(Long id);
    QnaPostResponse create(QnaPostRequest req, String username);
    QnaPostResponse update(Long id, QnaPostRequest req, String username);
    void delete(Long id, String username);
}
