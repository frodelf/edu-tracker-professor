package ua.kpi.edutrackerprofessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/course/**","/student/**","/task/**", "/literature/**"
                                ).authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(form ->
                        form.loginPage("/login")
                                .loginProcessingUrl("/process_login")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/login?error")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                );
        return http.build();
    }
}