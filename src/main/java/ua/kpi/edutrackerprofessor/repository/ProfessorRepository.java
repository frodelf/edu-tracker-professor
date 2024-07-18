package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByEmail(String email);
}