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

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /** 토큰 검증을 아예 스킵할 URL 패턴들 */
    private static final Set<String> WHITE_LIST = Set.of(
        "/api/auth/login",
        "/api/members/signup",
        "/api/problem-posts/all/**",
        "/api/problem-posts/search/**",
        "/local_image_storage/**",
        "/problem_post_data_storage/**",
        "/", "/index.html", "/css/**", "/js/**", "/static/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri    = request.getRequestURI();
        String method = request.getMethod();
        String base   = request.getContextPath(); // 보통 빈 문자열

        // 1) actuator 는 전부 스킵
        if (uri.startsWith(base + "/actuator/")) {
            return true;
        }

        // 2) 화이트리스트 패턴 스킵
        for (String pattern : WHITE_LIST) {
            if (pathMatcher.match(pattern, uri)) {
                return true;
            }
        }

        // 3) 팀모집 목록 조회(GET)만 스킵
        if ("GET".equalsIgnoreCase(method)
                && pathMatcher.match(base + "/api/team-recruits/**", uri)) {
            return true;
        }

        // 그 외는 필터 적용
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String headerAuth = request.getHeader("Authorization");
        log.debug("Authorization: {}", headerAuth);

        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            sendError(response, "ACCESS TOKEN NOT FOUND");
            return;
        }

        String accessToken = headerAuth.substring(7);
        try {
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);

            String username = (String) claims.get("username");
            Role role       = Role.valueOf((String) claims.get("role"));

            List<GrantedAuthority> authorities = role.getAuthorities().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    new CustomUserDetails(username, authorities),
                    null,
                    authorities
                );
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendError(response, e.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\":\"" + msg + "\"}");
    }
}
