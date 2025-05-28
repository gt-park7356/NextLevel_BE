package com.example.NextLevel.domain.teamRecruit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRecruitRequest {
    @NotBlank(message = "제목은 반드시 입력하세요.")
    private String title;

    @NotBlank(message = "내용은 반드시 입력하세요.")
    private String content;

    @NotBlank(message = "학교명은 반드시 입력하세요.")
    private String school;

    @NotBlank(message = "학과명은 반드시 입력하세요.")
    private String department;

    private String professor;
    private String courseName;
    private String description;
}
