package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.edutrackerentity.entity.enums.StatusCourse;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAllByProfessorIdAndStatusCourse(Long professorId, StatusCourse statusCourse, Pageable pageable);
}