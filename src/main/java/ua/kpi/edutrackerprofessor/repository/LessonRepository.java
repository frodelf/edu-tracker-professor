package ua.kpi.edutrackerprofessor.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    long countByCourseId(long courseId);
    long countAllByCourseIn(List<Course> courses);
    @Query(value = "SELECT DATE(l.date) AS date, COUNT(r.present) AS students FROM review r INNER JOIN lesson l ON r.lesson_id = l.id WHERE r.present = '1' GROUP BY DATE(l.date)", nativeQuery = true)
    List<Object[]> countStudentsByDate();
    @Query(value = "SELECT DATE(l.date) AS date, COUNT(r.present) AS students FROM review r INNER JOIN lesson l ON r.lesson_id = l.id WHERE r.present = '1' AND l.course_id = :courseId GROUP BY DATE(l.date)", nativeQuery = true)
    List<Object[]> countStudentsByDateAndCourseId(Long courseId);
}