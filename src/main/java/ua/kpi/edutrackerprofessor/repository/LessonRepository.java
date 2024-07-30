package ua.kpi.edutrackerprofessor.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.kpi.edutrackerentity.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    long countByCourseId(long courseId);
}