package com.example.NextLevel.domain.post.problem.dto.response;

import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemPostResponse {
    private String title;
    private String content;
    private String professorName;
    private String school;
    private String subject;
    private String problemData;
    private String createdAt;
    private String updatedAt;

    public ProblemPostResponse(ProblemPost problemPost) {
        this.title = problemPost.getTitle();
        this.content = problemPost.getContent();
        this.professorName = problemPost.getProfessorName();
        this.school = problemPost.getSchool();
        this.subject = problemPost.getSubject();
        this.problemData = problemPost.getProblemData();
        this.createdAt = problemPost.getCreatedAt().toString();
        this.updatedAt = problemPost.getUpdatedAt().toString();
    }
}
