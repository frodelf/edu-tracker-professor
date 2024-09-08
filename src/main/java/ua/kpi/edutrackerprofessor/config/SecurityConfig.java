package ua.kpi.edutrackerprofessor.config;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/course/**","/student/**","/task/**", "/literature/**", "/email/**", "/ws/**", "/"
                                ).authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/process_login")
                                .successHandler(authenticationSuccessHandler())
                                .failureUrl("/login?error")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                );
        return http.build();
    }
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String redirectUrl = Arrays.stream(request.getCookies())
                    .filter(cookie -> "redirect-url-teach".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .filter(value -> !StringUtils.isBlank(value))
                    .orElse("/edu-tracker/teach");
            response.sendRedirect(redirectUrl);
        };
    }
}