package ua.kpi.edutrackerprofessor.service;

import io.minio.errors.*;
import ua.kpi.edutrackerentity.entity.StudentsTask;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForLessonEdit;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface StudentsTaskService {
    long countAllByStudentId(Long studentId);
    long countAllDoneTaskByStudentId(Long studentId);
    long countAllNotDoneTaskByStudentId(Long studentId);
    long countAllByStudentIdAndCourseId(Long studentId, Long courseId);
    long countAllDoneTaskByStudentIdAndCourseId(Long studentId, Long courseId);
    long countAllNotDoneTaskByStudentIdAndCourseId(Long studentId, Long courseId);
    long countMarkByStudentIdAndCourseId(Long studentId, Long courseId);
    Page<StudentTaskResponseForViewAll> getAll(StudentTaskRequestForFilter studentTaskRequestForFilter);
    void cancelMark(Long studentTaskId) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    StudentsTask getById(Long id);
    StudentsTask save(StudentsTask studentsTask);
    void evaluate(Long studentTaskId, Double mark);
    List<StudentTaskResponseForLessonEdit> getAllByStudentIdAndLessonId(Long studentId, Long lessonId);
}