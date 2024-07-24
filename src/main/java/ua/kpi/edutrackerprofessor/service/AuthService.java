package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerprofessor.dto.professor.ProfessorForRegistrationDto;

public interface AuthService {
    boolean isAuthenticated();
    String registration(ProfessorForRegistrationDto professor);
}
