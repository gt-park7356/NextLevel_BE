// QnaAnswerResponse.java
package com.example.NextLevel.domain.qna.dto.response;

import com.example.NextLevel.domain.qna.entity.QnaAnswer;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QnaAnswerResponse {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public static QnaAnswerResponse of(QnaAnswer a) {
        return QnaAnswerResponse.builder()
            .id(a.getId())
            .content(a.getContent())
            .author(a.getAuthor())
            .createdAt(a.getCreatedAt())
            .build();
    }
}
