// QnaPost.java
package com.example.NextLevel.domain.qna.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="qna_posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class QnaPost {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) 
    private String title;

    @Column(nullable=false, columnDefinition="TEXT") 
    private String content;

    @Column(nullable=false) 
    private String author;

    @CreatedDate 
    private LocalDateTime createdAt;

    @LastModifiedDate 
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="post", cascade=CascadeType.REMOVE)
    private List<QnaAnswer> answers;
}
