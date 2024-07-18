package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.StudentsTask;
import ua.kpi.edutrackerentity.entity.enums.StatusStudentsTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentsTaskRepository extends JpaRepository<StudentsTask, Long>, JpaSpecificationExecutor<StudentsTask> {
    @Query("SELECT COALESCE(COUNT(st), 0) FROM StudentsTask st WHERE st.status IN (:statuses) AND st.student.id = :studentId")
    Long countByStatusesAndStudentId(Long studentId, List<StatusStudentsTask> statuses);
    @Query("SELECT COALESCE(COUNT(st), 0) FROM StudentsTask st WHERE st.status IN (:statuses) AND st.student.id = :studentId AND st.task.course.id = :courseId")
    Long countByStatusesAndStudentIdAndCourseId(Long studentId, Long courseId, List<StatusStudentsTask> statuses);
    @Query("SELECT COALESCE(COUNT(st), 0) FROM StudentsTask st WHERE st.student.id = :studentId")
    Long countByStudentId(Long studentId);
    @Query("SELECT COALESCE(COUNT(st), 0) FROM StudentsTask st WHERE st.student.id = :studentId AND st.task.course.id = :courseId")
    Long countByStudentIdAndCourseId(Long studentId, Long courseId);
    @Query("SELECT COALESCE(SUM(st.mark), 0) FROM StudentsTask st WHERE st.student.id = :studentId AND st.task.course.id = :courseId")
    Long countMarkByStudentIdAndCourseId(Long studentId, Long courseId);
}