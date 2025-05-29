package com.example.NextLevel.domain.post.problem.service;

import com.example.NextLevel.domain.post.problem.dto.request.ProblemPostRequest;
import com.example.NextLevel.domain.post.problem.dto.response.ProblemPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProblemPostService {
    ProblemPostResponse register(ProblemPostRequest request, MultipartFile problemData, String username);
    ProblemPostResponse read(Long postId);
    Page<ProblemPostResponse> getAllPosts(Pageable pageable);
    Page<ProblemPostResponse> search(String title, String professorName, String subject, String school, Pageable pageable);
    void delete(Long postId, String username);
    ProblemPostResponse update(Long postId, ProblemPostRequest request, MultipartFile problemData, String username);
}
