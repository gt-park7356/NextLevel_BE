package com.example.NextLevel.domain.teamRecruit.dto.response;

import com.example.NextLevel.domain.teamRecruit.entity.TeamRecruit;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRecruitResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TeamRecruitResponse of(TeamRecruit tr) {
        return new TeamRecruitResponse(
            tr.getId(), tr.getTitle(), tr.getContent(),
            tr.getAuthor(), tr.getCreatedAt(), tr.getUpdatedAt()
        );
    }
}
