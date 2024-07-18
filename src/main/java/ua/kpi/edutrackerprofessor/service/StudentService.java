package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerprofessor.dto.ForSelect2Dto;
import ua.kpi.edutrackerprofessor.dto.student.StudentRequestFilter;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseForAdd;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseViewAll;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseViewOnePage;
import ua.kpi.edutrackerentity.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Student save(Student student);
    long count();
    Student getById(long id);
    Page<StudentResponseViewAll> getAllByCourseList(int page, int pageSize, StudentRequestFilter studentRequestFilter);
    Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto);
    StudentResponseViewOnePage getByIdForView(Long id);
    List<StudentResponseForAdd> getAllByGroupAndCourse(String group, Long courseId);
}