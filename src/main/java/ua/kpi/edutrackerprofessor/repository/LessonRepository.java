package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    long countByCourseId(long courseId);
}