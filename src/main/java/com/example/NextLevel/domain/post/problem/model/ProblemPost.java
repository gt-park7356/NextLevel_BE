package com.example.NextLevel.domain.post.problem.model;

import com.example.NextLevel.common.BaseTimeEntity;
import com.example.NextLevel.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="problem_post")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String professorName;
    private String school;
    private String subject;

    private String problemData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

}
