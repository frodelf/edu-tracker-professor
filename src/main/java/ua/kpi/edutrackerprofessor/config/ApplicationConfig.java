package ua.kpi.edutrackerprofessor.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kpi.edutrackerprofessor.auth.UserDetailsImpl;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationConfig {

    private final ProfessorService professorService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> new UserDetailsImpl().getUserDetailsByUsers(professorService.getByEmailForAuth(username));
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}