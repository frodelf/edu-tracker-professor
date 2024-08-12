package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorDtoForRegistration;
import ua.kpi.edutrackerprofessor.mapper.ProfessorMapping;
import ua.kpi.edutrackerprofessor.service.AuthService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ProfessorMapping professorMapping = new ProfessorMapping();
    private final ProfessorService professorService;
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
    @Override
    @Transactional
    public void registration(ProfessorDtoForRegistration professorDtoForRegistration) {
        Professor professor = professorMapping.toEntityForRegistration(professorDtoForRegistration);
        professorService.save(professor);
    }
}
