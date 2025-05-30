// QnaAnswerRequest.java
package com.example.NextLevel.domain.qna.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaAnswerRequest {
    @NotBlank(message="답변 내용을 입력하세요.")
    private String content;
}
