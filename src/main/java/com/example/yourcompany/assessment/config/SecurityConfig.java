package com.example.yourcompany.assessment.config;
import com.example.yourcompany.assessment.security.JwtRequestFilter;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    TODO 这里注释掉了

    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println(36);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
//
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println(42);
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/api").permitAll()
//                TODO   权限控制 这里因为已经配置跟路径是api了  所以不需要加api
//                .antMatchers("/users/**").permitAll()
//
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/auth/register").permitAll()
//                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
//                TODO 这个是最关键的
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"未授权\", \"message\": \"" + authException.getMessage() + "\"}");
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//        TODO 这里注释掉了

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        System.out.println(71);
        return super.authenticationManagerBean();
    }
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println(77);
        return new BCryptPasswordEncoder();
    }
}