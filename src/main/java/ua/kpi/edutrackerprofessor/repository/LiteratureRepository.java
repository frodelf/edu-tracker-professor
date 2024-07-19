package ua.kpi.edutrackerprofessor.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.kpi.edutrackerentity.entity.Literature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiteratureRepository extends JpaRepository<Literature, Long>, JpaSpecificationExecutor<Literature> {
}