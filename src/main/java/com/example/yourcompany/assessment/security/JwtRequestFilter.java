package com.example.yourcompany.assessment.security;

//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;



//    @Override
//    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtTokenUtil.extractUsername(jwt);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //TODO 这个jwt要好好搞搞  跳过登录和注册请求 前端请求提那边添加一个标识头  然后后端如果找到这个特定的标识头部 则直接放行
        //        System.out.println("过滤器开始设置");
        //
        final String authorizationHeader = request.getHeader("Authorization");
        //
        String username = null;
        String jwt = null;
        String tmp = "GUEST_TOKEN";

        System.out.println(authorizationHeader.substring(7));

        if (authorizationHeader.substring(7).equals("guest_token")) {
            //            System.out.println("guest token  "+authorizationHeader.substring(7));
        }
        //
        if (authorizationHeader != null && authorizationHeader.substring(7).equals("guest_token")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            //            System.out.println(jwt);
            username = jwtTokenUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
    

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    //         throws ServletException, IOException {
    //     final String authorizationHeader = request.getHeader("Authorization");

    //     String username = null;
    //     String jwt = null;

    //     // 提取令牌
    //     if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
    //         jwt = authorizationHeader.substring(7);
    //         username = jwtTokenUtil.extractUsername(jwt);
    //     }

    //     // 验证令牌并设置安全上下文
    //     if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    //         UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

    //         if (jwtTokenUtil.validateToken(jwt, userDetails)) {
    //             UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
    //                     userDetails, null, userDetails.getAuthorities());
    //             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //             SecurityContextHolder.getContext().setAuthentication(authToken);
    //         }
    //     }
    //     filterChain.doFilter(request, response);
    // }

    
}