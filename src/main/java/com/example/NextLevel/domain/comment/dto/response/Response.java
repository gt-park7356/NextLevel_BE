package com.example.NextLevel.domain.comment.dto.response;

import com.example.NextLevel.domain.comment.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Response {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private Long parentId;
    private List<Response> replies;

    public static Response of(Comment c) {
        List<Response> children = c.getReplies().stream()
                .map(Response::of)
                .collect(Collectors.toList());
        return new Response(
                c.getId(),
                c.getContent(),
                c.getMember().getUsername(),
                c.getCreatedAt(),
                c.getParent() != null ? c.getParent().getId() : null,
                children
        );
    }
}
