package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorRequestForPersonalData;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForGlobal;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForPersonalData;

public interface ProfessorService {
    Professor save(Professor professor);
    long count();
    Professor getByEmailForAuth(String username);
    Professor getById(long id);
    Professor getByEmail(String email);
    Professor getAuthProfessor();
    ProfessorResponseForGlobal getAuthProfessorForGlobal();
    ProfessorResponseForPersonalData getAuthProfessorForPersonalData();
    void updatePersonalData(ProfessorRequestForPersonalData personalData);
}