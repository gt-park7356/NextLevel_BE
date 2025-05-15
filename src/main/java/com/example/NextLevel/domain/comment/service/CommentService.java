package com.example.NextLevel.domain.comment.service;

import com.example.NextLevel.common.exception.CustomException;
import com.example.NextLevel.common.exception.ErrorCode;
import com.example.NextLevel.domain.comment.dto.request.Request;
import com.example.NextLevel.domain.comment.dto.response.Response;
import com.example.NextLevel.domain.comment.model.Comment;
import com.example.NextLevel.domain.comment.repository.CommentRepository;
import com.example.NextLevel.domain.member.model.Member;
import com.example.NextLevel.domain.member.repository.MemberRepository;
import com.example.NextLevel.domain.post.problem.model.ProblemPost;
import com.example.NextLevel.domain.post.problem.repository.ProblemPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProblemPostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long addComment(Long postId, Request req, String username) {
        ProblemPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));   // :contentReference[oaicite:8]{index=8}:contentReference[oaicite:9]{index=9}

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));  // :contentReference[oaicite:10]{index=10}:contentReference[oaicite:11]{index=11}

        Comment parent = null;
        if (req.getParentId() != null) {
            parent = commentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT));
        }

        Comment comment = Comment.builder()
                .content(req.getContent())
                .member(member)
                .post(post)
                .parent(parent)
                .build();

        return commentRepository.save(comment).getId();
    }

    public List<Response> getComments(Long postId) {
        ProblemPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findByPostAndParentIsNullOrderByCreatedAtAsc(post).stream()
                .map(Response::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateComment(Long commentId, String content, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT));
        if (!comment.getMember().getUsername().equals(username)) {
            throw new CustomException(ErrorCode.FORBIDDEN_POST);
        }
        comment.updateContent(content);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT));
        if (!comment.getMember().getUsername().equals(username)) {
            throw new CustomException(ErrorCode.FORBIDDEN_POST);
        }
        commentRepository.delete(comment);
    }

}
