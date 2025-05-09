package com.example.NextLevel.domain.post.problem.dto.request;

import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ProblemPostRequest {
    private String title;
    private String content;
    private String professorName;
    private String school;
    private String subject;
    private String problemData;

    public ProblemPost toEntity(Member member) {
        return ProblemPost.builder()
                .title(title)
                .content(content)
                .professorName(professorName)
                .school(school)
                .subject(subject)
                .member(member)
                .problemData(problemData)
                .build();
    }
}
