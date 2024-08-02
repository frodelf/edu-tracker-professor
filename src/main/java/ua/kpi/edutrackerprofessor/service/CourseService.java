package ua.kpi.edutrackerprofessor.service;

import io.minio.errors.*;
import ua.kpi.edutrackerprofessor.dto.course.CourseDtoForAdd;
import ua.kpi.edutrackerprofessor.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackerentity.entity.Course;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface CourseService {
    Page<CourseResponseViewAll> getAll(int page, int pageSize);
    void removeById(Long courseId);
    Course getById(Long courseId);
    Course save(Course course);
    long add(CourseDtoForAdd courseDtoForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    void isCourseAssignedToProfessor(long courseId);
    CourseDtoForAdd getByIdForAdd(Long courseId);
    Map<String, String> getForSelect();
    Map<String, String> getForSelectByStudent(Long studentId);
    void addStudentToCourse(Map<String, String> students, Long courseId);
    void removeStudentFromCourse(Long studentId, Long courseId);
    Integer getCountCourse();
}