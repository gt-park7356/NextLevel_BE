package com.example.NextLevel.domain.comment.model;

import com.example.NextLevel.common.BaseTimeEntity;
import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    // 댓글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 어느 게시글에 속한 댓글인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private ProblemPost post;

    // 부모 댓글 (null 이면 최상위 댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글 목록
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @Builder
    private Comment(String content, Member member, ProblemPost post, Comment parent) {
        this.content = content;
        this.member  = member;
        this.post    = post;
        this.parent  = parent;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
