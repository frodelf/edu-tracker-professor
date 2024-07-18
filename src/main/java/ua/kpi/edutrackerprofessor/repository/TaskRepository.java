package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findDistinctByCourseIn(List<Course> courses, Pageable pageable);
}