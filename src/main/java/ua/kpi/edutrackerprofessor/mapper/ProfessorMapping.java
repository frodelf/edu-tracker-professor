package ua.kpi.edutrackerprofessor.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorDtoForRegistration;

public class ProfessorMapping {
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