package ua.kpi.edutrackerprofessor.service.impl;

import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForLessonEdit;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerentity.entity.StudentsTask;
import ua.kpi.edutrackerentity.entity.enums.StatusStudentsTask;
import ua.kpi.edutrackerprofessor.mapper.StudentTaskMapper;
import ua.kpi.edutrackerprofessor.repository.StudentsTaskRepository;
import ua.kpi.edutrackerprofessor.service.LessonService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;
import ua.kpi.edutrackerprofessor.specification.StudentsTaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentsTaskServiceImpl implements StudentsTaskService {
    private final StudentsTaskRepository studentsTaskRepository;
    private final ProfessorService professorService;
    private final LessonService lessonService;
    private final StudentTaskMapper studentTaskMapper = new StudentTaskMapper();
    @Override
    public long countAllByStudentId(Long studentId) {
        return studentsTaskRepository.countByStudentId(studentId);
    }
    @Override
    public long countAllDoneTaskByStudentId(Long studentId) {
        return studentsTaskRepository.countByStatusesAndStudentId(studentId, List.of(StatusStudentsTask.EVALUATED, StatusStudentsTask.GRANTED));
    }
    @Override
    public long countAllNotDoneTaskByStudentId(Long studentId) {
        return studentsTaskRepository.countByStatusesAndStudentId(studentId, List.of(StatusStudentsTask.IN_PROCESS));
    }
    @Override
    public long countAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        return studentsTaskRepository.countByStudentIdAndCourseId(studentId, courseId);
    }
    @Override
    public long countAllDoneTaskByStudentIdAndCourseId(Long studentId, Long courseId) {
        return studentsTaskRepository.countByStatusesAndStudentIdAndCourseId(studentId, courseId, List.of(StatusStudentsTask.EVALUATED, StatusStudentsTask.GRANTED));
    }
    @Override
    public long countAllNotDoneTaskByStudentIdAndCourseId(Long studentId, Long courseId) {
        return studentsTaskRepository.countByStatusesAndStudentIdAndCourseId(studentId, courseId, List.of(StatusStudentsTask.IN_PROCESS));
    }
    @Override
    public long countMarkByStudentIdAndCourseId(Long studentId, Long courseId) {
        return studentsTaskRepository.countMarkByStudentIdAndCourseId(studentId, courseId);
    }
    @Override
    public Page<StudentTaskResponseForViewAll> getAll(StudentTaskRequestForFilter studentTaskRequestForFilter) {
        Pageable pageable = PageRequest.of(studentTaskRequestForFilter.getPage(), studentTaskRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Specification<StudentsTask> specification = new StudentsTaskSpecification(studentTaskRequestForFilter, professorService.getAuthProfessor().getCourses());
        return studentTaskMapper.toDtoListForViewAll(studentsTaskRepository.findAll(specification, pageable));
    }
    @Override
    @Transactional
    public void cancelMark(Long studentTaskId) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StudentsTask studentsTask = getById(studentTaskId);
        studentsTask.setStatus(StatusStudentsTask.GRANTED);
        studentsTask.setMark(null);
        save(studentsTask);
    }
    @Override
    public StudentsTask getById(Long id) {
        return studentsTaskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("StudentTask with id = "+id+" not found")
        );
    }
    @Override
    @Transactional
    public StudentsTask save(StudentsTask studentsTask) {
        return studentsTaskRepository.save(studentsTask);
    }
    @Override
    @Transactional
    public void evaluate(Long studentTaskId, Double mark) {
        StudentsTask studentsTask = getById(studentTaskId);
        studentsTask.setMark(mark);
        studentsTask.setStatus(StatusStudentsTask.EVALUATED);
        save(studentsTask);
    }
    @Override
    public List<StudentTaskResponseForLessonEdit> getAllByStudentIdAndLessonId(Long studentId, Long lessonId) {
        Lesson lesson = lessonService.getById(lessonId);
        return studentTaskMapper.toDtoListForLessonAdd(studentsTaskRepository.findAllByStudentIdAndCourseId(studentId, lesson.getCourse().getId()));
    }
}
