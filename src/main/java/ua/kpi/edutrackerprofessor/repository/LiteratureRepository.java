package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Literature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiteratureRepository extends JpaRepository<Literature, Long> {
    Page<Literature> findDistinctByCourseIn(List<Course> courses, Pageable pageable);
}