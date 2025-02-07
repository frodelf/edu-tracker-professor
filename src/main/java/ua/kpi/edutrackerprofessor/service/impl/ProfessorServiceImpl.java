package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;
    private final MinioService minioService;
    private final ProfessorMapper professorMapper = new ProfessorMapper();
    @Override
    @Transactional
    public Professor save(Professor professor) {
        log.info("ProfessorServiceImpl save start");
        Professor savedProfessor = professorRepository.save(professor);
        log.info("ProfessorServiceImpl save finish");
        return savedProfessor;
    }

    @Override
    public long count() {
        log.info("ProfessorServiceImpl count start");
        long result = professorRepository.count();
        log.info("ProfessorServiceImpl count finish");
        return result;
    }

    @Override
    public Professor getByEmailForAuth(String username) {
        log.info("ProfessorServiceImpl getByEmailForAuth start");
        Professor professor = professorRepository.findByEmail(username).orElseThrow(
                () -> new AuthenticationCredentialsNotFoundException("Credential isn't correct")
        );
        log.info("ProfessorServiceImpl getByEmailForAuth finish");
        return professor;
    }

    @Override
    public Professor getById(long id) {
        log.info("ProfessorServiceImpl getById start");
        Professor professor = professorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Professor with id = "+id+" not found")
        );
        log.info("ProfessorServiceImpl getById finish");
        return professor;
    }

    @Override
    public Professor getByEmail(String email) {
        log.info("ProfessorServiceImpl getByEmail start");
        Professor professor = professorRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Professor with email = "+email+" not found")
        );
        log.info("ProfessorServiceImpl getByEmail finish");
        return professor;
    }

    @Override
    public Professor getAuthProfessor() {
        log.info("ProfessorServiceImpl getAuthProfessor start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            String currentUserName = authentication.getName();
            Professor professor = getByEmail(currentUserName);
            log.info("ProfessorServiceImpl getAuthProfessor finish");
            return professor;
        } else {
            log.info("ProfessorServiceImpl getAuthProfessor failed");
            throw new InsufficientAuthenticationException("The professor is not authorized");
        }
    }

    @Override
    public ProfessorResponseForGlobal getAuthProfessorForGlobal() {
        log.info("ProfessorServiceImpl getAuthProfessorForGlobal start");
        try {
            ProfessorResponseForGlobal response = professorMapper.toDtoForGlobal(getAuthProfessor());
            log.info("ProfessorServiceImpl getAuthProfessorForGlobal finish");
            return response;
        } catch (InsufficientAuthenticationException e) {
            log.info("ProfessorServiceImpl getAuthProfessorForGlobal failed");
            return null;
        }
    }

    @Override
    public ProfessorResponseForPersonalData getAuthProfessorForPersonalData() {
        log.info("ProfessorServiceImpl getAuthProfessorForPersonalData start");
        ProfessorResponseForPersonalData response = professorMapper.toDtoForPersonalData(getAuthProfessor(), minioService);
        log.info("ProfessorServiceImpl getAuthProfessorForPersonalData finish");
        return response;
    }

    @Override
    @Transactional
    @SneakyThrows
    //TODO Доробити видалення файлу з мініо
    public void updatePersonalData(ProfessorRequestForPersonalData personalData) {
        log.info("ProfessorServiceImpl updatePersonalData start");
        Professor professor = professorMapper.toEntityFromPersonalDataDto(personalData, this);
        if (nonNull(personalData.getImage())) {
            professor.setImage(minioService.putMultipartFile(personalData.getImage()));
        }
        save(professor);
        log.info("ProfessorServiceImpl updatePersonalData finish");
    }

    @Override
    public boolean isAuthenticated() {
        log.info("ProfessorServiceImpl isAuthenticated start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        boolean result = !(authentication instanceof AnonymousAuthenticationToken);
        log.info("ProfessorServiceImpl isAuthenticated start");
        return result;
    }
    @Override
    @Transactional
    public void registration(ProfessorDtoForRegistration professorDtoForRegistration) {
        log.info("ProfessorServiceImpl registration start");
        Professor professor = professorMapper.toEntityForRegistration(professorDtoForRegistration);
        save(professor);
        log.info("ProfessorServiceImpl registration finish");
    }
}
