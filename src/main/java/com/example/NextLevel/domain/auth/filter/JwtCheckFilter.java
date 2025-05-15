package com.example.NextLevel.domain.auth.filter;

import com.example.NextLevel.domain.auth.principal.CustomUserDetails;
import com.example.NextLevel.domain.auth.util.JWTUtil;
import com.example.NextLevel.domain.member.model.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("--- shouldNotFilter()");
        log.info("--- requestURI : " + request.getRequestURI());

        // 제외할 경로 리스트
        Set<String> excludedPaths = Set.of(
                "/api/auth/login",
                "/api/members/signup",
                "/api/problem-posts/all/**",
                "/api/problem-posts/search/**",
                "/local_image_storage/**",
                "/problem_post_data_storage/**",
                "/", "/static/**", "/index.html", "/css/**", "/js/**"
        );

        // 요청 URI가 제외할 경로 패턴에 매칭되면 필터링을 적용하지 않음
        for (String path : excludedPaths) {
            if (pathMatcher.match(path, request.getRequestURI())) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("--- doFilterInternal() ");
        log.info("--- requestURI : " + request.getRequestURI());

        // Authorization 헤더에서 액세스 토큰 읽기
        String headerAuth = request.getHeader("Authorization");
        log.info("--- headerAuth : " + headerAuth);

        //액세스 토큰이 없거나 'Bearer ' 가 아니면 403 예외 발생
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {   //Bearer 옆에 공백 필수 !
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));
            return;
        }

        // 토큰 유효성 검증 --------------------------------------
        String accessToken = headerAuth.substring(7);  //"Bearer " 를 제외하고 토큰값 저장
        try{
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("--- 토큰 유효성 검증 완료 ---");

            //SecurityContext 처리 ------------------------------------------
            String username = claims.get("username").toString();
            String roleString = claims.get("role").toString();
            Role role = Role.valueOf(roleString);

            Set<Role> authorities = role.getAuthorities();

            authorities.forEach(authority -> log.info("beforeAuthority: " + authority.name()));

            // Spring Security의 권한 객체로 변환
            List<GrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                    .collect(Collectors.toList());

            grantedAuthorities.forEach(authority -> logger.info("Granted Authority: "+ authority.getAuthority()));

            //토큰을 이용하여 인증된 정보 저장
            setAuthentication(username, grantedAuthorities);

            filterChain.doFilter(request, response); //검증 결과 문제가 없는 경우
        }catch (Exception e) {
            handleException(response, e);
        }
    }

    private void setAuthentication(String username, List<GrantedAuthority> grantedAuthorities) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(username, grantedAuthorities), null, grantedAuthorities
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
    }

}
