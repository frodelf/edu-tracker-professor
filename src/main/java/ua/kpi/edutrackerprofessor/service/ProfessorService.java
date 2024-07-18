package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerentity.entity.Professor;

public interface ProfessorService {
    Professor save(Professor professor);
    long count();
    Professor getByEmailForAuth(String username);
    Professor getById(long id);
    Professor getByEmail(String email);
    Professor getAuthProfessor();
}