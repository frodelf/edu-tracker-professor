package ua.kpi.edutrackerprofessor.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    @Query("SELECT COALESCE(COUNT(rw), 0) FROM Review rw WHERE rw.present = true AND rw.student.id = :studentId AND rw.lesson.course.id = :courseId")
    Long countByStudentIdAndCourseId(Long studentId, Long courseId);
    @Query("SELECT COALESCE(COUNT(rw), 0) FROM Review rw WHERE rw.present = true AND rw.student.id = :studentId")
    Long countByStudentId(Long studentId);
    Long countByLessonAndPresentTrue(Lesson lesson);
    List<Review> findAllByLessonId(Long lessonId);
}