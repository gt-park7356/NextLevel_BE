package com.example.NextLevel.domain.post.problem.service;

import com.example.NextLevel.domain.post.problem.dto.request.ProblemPostRequest;
import com.example.NextLevel.domain.post.problem.dto.response.ProblemPostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProblemPostService {
    public ProblemPostResponse register(ProblemPostRequest request, MultipartFile problemData, String username);
    public ProblemPostResponse read(Long postId);
}
