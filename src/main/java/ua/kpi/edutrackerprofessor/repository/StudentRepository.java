package ua.kpi.edutrackerprofessor.repository;

import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Page<Student> findDistinctByCoursesIn(List<Course> courses, Pageable pageable);
    @Query("SELECT DISTINCT s.groupName FROM Student s WHERE LOWER(s.groupName) LIKE LOWER(CONCAT('%', :groupName, '%'))")
    Page<String> findAllGroupNamesByGroupNameLikeIgnoreCase(@Param("groupName") String groupName, Pageable pageable);
    List<Student> findAllByGroupName(String groupName);
    @Query("SELECT s.email FROM Student s WHERE s.groupName = :groupName")
    List<String> findAllEmailsByGroupName(String groupName);
}
