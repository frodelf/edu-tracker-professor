package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerprofessor.dto.professor.ProfessorForRegistrationDto;

public interface AuthService {
    boolean isAuthenticated();
    void registration(ProfessorForRegistrationDto professor);
}
