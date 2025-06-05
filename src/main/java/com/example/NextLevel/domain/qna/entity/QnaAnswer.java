// QnaAnswer.java
package com.example.NextLevel.domain.qna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Entity
@Table(name="qna_answers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QnaAnswer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, columnDefinition="TEXT")
    private String content;

    @Column(nullable=false)
    private String author;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private QnaPost post;
}
