package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findDistinctByCourseIn(List<Course> courses, Pageable pageable);
    List<Task> findAllByCourseIdAndStatus(Long courseId, StatusTask status);
    Long countAllByCourseId(Long courseId);
    Long countAllByCourseIdAndStatus(Long courseId, StatusTask status);
}