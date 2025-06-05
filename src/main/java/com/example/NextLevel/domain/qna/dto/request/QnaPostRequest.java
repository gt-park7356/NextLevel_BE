// QnaPostRequest.java
package com.example.NextLevel.domain.qna.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaPostRequest {
    @NotBlank(message="제목은 반드시 입력하세요.")
    private String title;

    @NotBlank(message="내용은 반드시 입력하세요.")
    private String content;
}
