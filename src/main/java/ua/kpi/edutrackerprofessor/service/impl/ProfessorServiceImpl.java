package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorDtoForRegistration;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorRequestForPersonalData;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForGlobal;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForPersonalData;
import ua.kpi.edutrackerprofessor.mapper.ProfessorMapper;
import ua.kpi.edutrackerprofessor.repository.ProfessorRepository;
import ua.kpi.edutrackerprofessor.service.MinioService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;
    private final MinioService minioService;
    private final ProfessorMapper professorMapper = new ProfessorMapper();
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
    @Override
    public ProfessorResponseForGlobal getAuthProfessorForGlobal() {
        try {
            return professorMapper.toDtoForGlobal(getAuthProfessor());
        }catch (InsufficientAuthenticationException e){
            return null;
        }
    }
    @Override
    public ProfessorResponseForPersonalData getAuthProfessorForPersonalData() {
        return professorMapper.toDtoForPersonalData(getAuthProfessor(), minioService);
    }
    @Override
    @Transactional
    @SneakyThrows
    //TODO Доробити видалення файлу з мініо
    public void updatePersonalData(ProfessorRequestForPersonalData personalData) {
        Professor professor = professorMapper.toEntityFromPersonalDataDto(personalData, this);
        if(nonNull(personalData.getImage())){
            professor.setImage(minioService.putMultipartFile(personalData.getImage()));
        }
        save(professor);
    }

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
        Professor professor = professorMapper.toEntityForRegistration(professorDtoForRegistration);
        save(professor);
    }
}