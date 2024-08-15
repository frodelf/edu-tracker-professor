package ua.kpi.edutrackerprofessor.mapper;

import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorDtoForRegistration;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorRequestForPersonalData;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForGlobal;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForPersonalData;
import ua.kpi.edutrackerprofessor.service.MinioService;
import ua.kpi.edutrackerprofessor.service.impl.ProfessorServiceImpl;

import static java.util.Objects.nonNull;

public class ProfessorMapper {
    public ProfessorResponseForGlobal toDtoForGlobal(Professor authProfessor) {
        ProfessorResponseForGlobal response = new ProfessorResponseForGlobal();
        response.setId(authProfessor.getId());
        response.setName(authProfessor.getName());
        response.setLastName(authProfessor.getLastName());
        response.setImage(authProfessor.getImage());
        response.setEmail(authProfessor.getEmail());
        response.setDegree(authProfessor.getDegree());
        return response;
    }
    @SneakyThrows
    public ProfessorResponseForPersonalData toDtoForPersonalData(Professor authProfessor, MinioService minioService) {
        ProfessorResponseForPersonalData professorResponseForPersonalData = new ProfessorResponseForPersonalData();
        professorResponseForPersonalData.setId(authProfessor.getId());
        professorResponseForPersonalData.setName(authProfessor.getName());
        professorResponseForPersonalData.setLastName(authProfessor.getLastName());
        professorResponseForPersonalData.setMiddleName(authProfessor.getMiddleName());
        professorResponseForPersonalData.setEmail(authProfessor.getEmail());
        if(authProfessor.getImage()!=null){
            professorResponseForPersonalData.setImage(minioService.getUrl(authProfessor.getImage()));
        }
        professorResponseForPersonalData.setDegree(authProfessor.getDegree());
        professorResponseForPersonalData.setPhone(authProfessor.getPhone());
        professorResponseForPersonalData.setTelegram(authProfessor.getTelegram());
        return professorResponseForPersonalData;
    }

    public Professor toEntityFromPersonalDataDto(ProfessorRequestForPersonalData personalData, ProfessorServiceImpl professorService) {
        Professor professor = new Professor();
        if(nonNull(personalData.getId())){
            professor = professorService.getById(personalData.getId());
        }
        professor.setMiddleName(personalData.getMiddleName());
        professor.setName(personalData.getName());
        professor.setLastName(personalData.getLastName());
        professor.setDegree(personalData.getDegree());
        professor.setPhone(personalData.getPhone());
        professor.setEmail(personalData.getEmail());
        professor.setTelegram(personalData.getTelegram());
        return professor;
    }

    public Professor toEntityForRegistration(ProfessorDtoForRegistration professorDtoForRegistration) {
        Professor professor = new Professor();
        professor.setId(professorDtoForRegistration.getId());
        professor.setLastName(professorDtoForRegistration.getLastName());
        professor.setName(professorDtoForRegistration.getName());
        professor.setMiddleName(professorDtoForRegistration.getMiddleName());
        professor.setEmail(professorDtoForRegistration.getEmail());
        professor.setDegree(professorDtoForRegistration.getDegree());
        professor.setPhone(professorDtoForRegistration.getPhone());
        professor.setTelegram(professorDtoForRegistration.getTelegram());
        professor.setPassword(new BCryptPasswordEncoder().encode(professorDtoForRegistration.getPasswordForRegistration()));
        return professor;
    }
}