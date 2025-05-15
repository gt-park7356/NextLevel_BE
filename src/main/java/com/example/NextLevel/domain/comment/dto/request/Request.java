package com.example.NextLevel.domain.comment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String content;
    private Long parentId;  // 대댓글일 경우 부모 댓글 ID
}
