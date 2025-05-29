package com.example.NextLevel.domain.teamRecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_recruits")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class TeamRecruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String department;

    @Column(nullable = true)
    private String professor;

    @Column(nullable = true)
    private String courseName;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String author;  // username

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
