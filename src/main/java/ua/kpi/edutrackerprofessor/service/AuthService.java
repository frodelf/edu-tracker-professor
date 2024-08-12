package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerprofessor.dto.professor.ProfessorDtoForRegistration;

public interface AuthService {
    boolean isAuthenticated();
    void registration(ProfessorDtoForRegistration professor);
}
