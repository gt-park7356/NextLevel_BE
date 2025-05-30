// QnaPostResponse.java
package com.example.NextLevel.domain.qna.dto.response;

import com.example.NextLevel.domain.qna.entity.QnaPost;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QnaPostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QnaAnswerResponse> answers;

    public static QnaPostResponse of(QnaPost p, List<QnaAnswerResponse> ans) {
        return QnaPostResponse.builder()
            .id(p.getId())
            .title(p.getTitle())
            .content(p.getContent())
            .author(p.getAuthor())
            .createdAt(p.getCreatedAt())
            .updatedAt(p.getUpdatedAt())
            .answers(ans)
            .build();
    }
}
