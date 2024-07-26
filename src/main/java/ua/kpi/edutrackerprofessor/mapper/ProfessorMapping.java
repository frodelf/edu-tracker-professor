package ua.kpi.edutrackerprofessor.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorForRegistrationDto;

public class ProfessorMapping {
    public Professor toEntityForRegistration(ProfessorForRegistrationDto professorForRegistrationDto) {
        Professor professor = new Professor();
        professor.setId(professorForRegistrationDto.getId());
        professor.setLastName(professorForRegistrationDto.getLastName());
        professor.setName(professorForRegistrationDto.getName());
        professor.setMiddleName(professorForRegistrationDto.getMiddleName());
        professor.setEmail(professorForRegistrationDto.getEmail());
        professor.setDegree(professorForRegistrationDto.getDegree());
        professor.setPhone(professorForRegistrationDto.getPhone());
        professor.setTelegram(professorForRegistrationDto.getTelegram());
        professor.setPassword(new BCryptPasswordEncoder().encode(professorForRegistrationDto.getPasswordForRegistration()));
        return professor;
    }
}