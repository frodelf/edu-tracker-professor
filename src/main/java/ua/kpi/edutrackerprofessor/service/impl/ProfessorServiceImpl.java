package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.repository.ProfessorRepository;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;
    @Override
    @Transactional
    public Professor save(Professor professor) {
        return professorRepository.save(professor);
    }
    @Override
    public long count() {
        return professorRepository.count();
    }
    @Override
    public Professor getByEmailForAuth(String username) {
        return professorRepository.findByEmail(username).orElseThrow(
                () -> new AuthenticationCredentialsNotFoundException("Credential isn't correct")
        );
    }
    @Override
    public Professor getById(long id) {
        return professorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Professor with id = "+id+" not found")
        );
    }
    @Override
    public Professor getByEmail(String email) {
        return professorRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Professor with email = "+email+" not found")
        );
    }
    @Override
    public Professor getAuthProfessor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            String currentUserName = authentication.getName();
            return getByEmail(currentUserName);
        }
        else throw new InsufficientAuthenticationException("The professor is not authorized");
    }
}
