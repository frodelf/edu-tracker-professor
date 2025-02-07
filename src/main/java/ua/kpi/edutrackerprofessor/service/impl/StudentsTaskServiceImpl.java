package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerentity.entity.Task;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForLessonEdit;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerentity.entity.StudentsTask;
import ua.kpi.edutrackerentity.entity.enums.StatusStudentsTask;
import ua.kpi.edutrackerprofessor.mapper.StudentTaskMapper;
import ua.kpi.edutrackerprofessor.repository.LessonRepository;
import ua.kpi.edutrackerprofessor.repository.StudentsTaskRepository;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;
import ua.kpi.edutrackerprofessor.specification.StudentsTaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class StudentsTaskServiceImpl implements StudentsTaskService {
    private final StudentsTaskRepository studentsTaskRepository;
    private final ProfessorService professorService;
    /*
     * I use the repository itself, because if I use the service,
     * an error is raised that indicates cyclic dependencies
     */
    private final LessonRepository lessonRepository;
    private final StudentTaskMapper studentTaskMapper = new StudentTaskMapper();

    @Override
    public long countAllByStudentId(Long studentId) {
        log.info("StudentsTaskServiceImpl countAllByStudentId start");
        long result = studentsTaskRepository.countByStudentId(studentId);
        log.info("StudentsTaskServiceImpl countAllByStudentId finish");
        return result;
    }

    @Override
    public long countAllDoneTaskByStudentId(Long studentId) {
        log.info("StudentsTaskServiceImpl countAllDoneTaskByStudentId start");
        long result = studentsTaskRepository.countByStatusesAndStudentId(studentId, List.of(StatusStudentsTask.EVALUATED, StatusStudentsTask.GRANTED));
        log.info("StudentsTaskServiceImpl countAllDoneTaskByStudentId finish");
        return result;
    }

    @Override
    public long countAllNotDoneTaskByStudentId(Long studentId) {
        log.info("StudentsTaskServiceImpl countAllNotDoneTaskByStudentId start");
        long result = studentsTaskRepository.countByStatusesAndStudentId(studentId, List.of(StatusStudentsTask.IN_PROCESS));
        log.info("StudentsTaskServiceImpl countAllNotDoneTaskByStudentId finish");
        return result;
    }

    @Override
    public long countAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        log.info("StudentsTaskServiceImpl countAllByStudentIdAndCourseId start");
        long result = studentsTaskRepository.countByStudentIdAndCourseId(studentId, courseId);
        log.info("StudentsTaskServiceImpl countAllByStudentIdAndCourseId finish");
        return result;
    }

    @Override
    public long countAllDoneTaskByStudentIdAndCourseId(Long studentId, Long courseId) {
        log.info("StudentsTaskServiceImpl countAllDoneTaskByStudentIdAndCourseId start");
        long result = studentsTaskRepository.countByStatusesAndStudentIdAndCourseId(studentId, courseId, List.of(StatusStudentsTask.EVALUATED, StatusStudentsTask.GRANTED));
        log.info("StudentsTaskServiceImpl countAllDoneTaskByStudentIdAndCourseId finish");
        return result;
    }

    @Override
    public long countAllNotDoneTaskByStudentIdAndCourseId(Long studentId, Long courseId) {
        log.info("StudentsTaskServiceImpl countAllNotDoneTaskByStudentIdAndCourseId start");
        long result = studentsTaskRepository.countByStatusesAndStudentIdAndCourseId(studentId, courseId, List.of(StatusStudentsTask.IN_PROCESS));
        log.info("StudentsTaskServiceImpl countAllNotDoneTaskByStudentIdAndCourseId finish");
        return result;
    }

    @Override
    public long countMarkByStudentIdAndCourseId(Long studentId, Long courseId) {
        log.info("StudentsTaskServiceImpl countMarkByStudentIdAndCourseId start");
        long result = studentsTaskRepository.countMarkByStudentIdAndCourseId(studentId, courseId);
        log.info("StudentsTaskServiceImpl countMarkByStudentIdAndCourseId finish");
        return result;
    }

    @Override
    public Page<StudentTaskResponseForViewAll> getAll(StudentTaskRequestForFilter studentTaskRequestForFilter) {
        log.info("StudentsTaskServiceImpl getAll start");
        Pageable pageable = PageRequest.of(studentTaskRequestForFilter.getPage(), studentTaskRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Specification<StudentsTask> specification = new StudentsTaskSpecification(studentTaskRequestForFilter, professorService.getAuthProfessor().getCourses());
        Page<StudentTaskResponseForViewAll> result = studentTaskMapper.toDtoListForViewAll(studentsTaskRepository.findAll(specification, pageable));
        log.info("StudentsTaskServiceImpl getAll finish");
        return result;
    }

    @Override
    @Transactional
    public void cancelMark(Long studentTaskId) {
        log.info("StudentsTaskServiceImpl cancelMark start");
        StudentsTask studentsTask = getById(studentTaskId);
        studentsTask.setStatus(StatusStudentsTask.GRANTED);
        studentsTask.setMark(null);
        save(studentsTask);
        log.info("StudentsTaskServiceImpl cancelMark finish");
    }

    @Override
    public StudentsTask getById(Long id) {
        log.info("StudentsTaskServiceImpl getById start");
        StudentsTask result = studentsTaskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("StudentTask with id = "+id+" not found")
        );
        log.info("StudentsTaskServiceImpl getById finish");
        return result;
    }

    @Override
    @Transactional
    public StudentsTask save(StudentsTask studentsTask) {
        log.info("StudentsTaskServiceImpl save start");
        StudentsTask result = studentsTaskRepository.save(studentsTask);
        log.info("StudentsTaskServiceImpl save finish");
        return result;
    }

    @Override
    @Transactional
    public void evaluate(Long studentTaskId, Double mark) {
        log.info("StudentsTaskServiceImpl evaluate start");
        StudentsTask studentsTask = getById(studentTaskId);
        studentsTask.setMark(mark);
        studentsTask.setStatus(StatusStudentsTask.EVALUATED);
        save(studentsTask);
        log.info("StudentsTaskServiceImpl evaluate finish");
    }

    @Override
    public List<StudentTaskResponseForLessonEdit> getAllByStudentIdAndLessonId(Long studentId, Long lessonId) {
        log.info("StudentsTaskServiceImpl getAllByStudentIdAndLessonId start");
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new EntityNotFoundException("Lesson with id = "+lessonId+" not found")
        );
        List<StudentTaskResponseForLessonEdit> result = studentTaskMapper.toDtoListForLessonAdd(studentsTaskRepository.findAllByStudentIdAndCourseId(studentId, lesson.getCourse().getId()));
        log.info("StudentsTaskServiceImpl getAllByStudentIdAndLessonId finish");
        return result;
    }

    @Override
    @Transactional
    public void triggerToOpenTask(Task task) {
        log.info("StudentsTaskServiceImpl triggerToOpenTask start");
        for (Student student : task.getCourse().getStudents()) {
            StudentsTask studentsTask = new StudentsTask();
            studentsTask.setStudent(student);
            studentsTask.setTask(task);
            studentsTask.setStatus(StatusStudentsTask.IN_PROCESS);
            save(studentsTask);
        }
        log.info("StudentsTaskServiceImpl triggerToOpenTask finish");
    }
}