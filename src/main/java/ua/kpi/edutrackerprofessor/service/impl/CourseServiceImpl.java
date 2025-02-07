package ua.kpi.edutrackerprofessor.service.impl;

import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ua.kpi.edutrackerentity.entity.enums.StatusCourse;
import ua.kpi.edutrackerprofessor.dto.course.CourseDtoForAdd;
import ua.kpi.edutrackerprofessor.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerprofessor.mapper.CourseMapper;
import ua.kpi.edutrackerprofessor.repository.CourseRepository;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.MinioService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final MinioService minioService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final CourseMapper courseMapper = new CourseMapper();

    @Override
    public Page<CourseResponseViewAll> getAll(int page, int pageSize) {
        log.info("CourseServiceImpl getAll start");
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id")));
        Page<CourseResponseViewAll> result = courseMapper.toDtoListForView(courseRepository.findAllByProfessorIdAndStatusCourse(professorService.getAuthProfessor().getId(), StatusCourse.ACTIVE, pageable), minioService);
        log.info("CourseServiceImpl getAll finish");
        return result;
    }

    @Override
    public void removeById(Long courseId) {
        log.info("CourseServiceImpl removeById start");
        isCourseAssignedToProfessor(courseId);
        courseRepository.deleteById(courseId);
        log.info("CourseServiceImpl removeById finish");
    }

    @Override
    public Course getById(Long courseId) {
        log.info("CourseServiceImpl getById start");
        isCourseAssignedToProfessor(courseId);
        Course result = courseRepository.findById(courseId).orElseThrow(
                () -> new EntityNotFoundException("Course with id = " + courseId + " not found")
        );
        log.info("CourseServiceImpl getById finish");
        return result;
    }

    @Override
    @Transactional
    public Course save(Course course) {
        log.info("CourseServiceImpl save start");
        Course result = courseRepository.save(course);
        log.info("CourseServiceImpl save finish");
        return result;
    }

    @Override
    @Transactional
    public long add(CourseDtoForAdd courseDtoForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("CourseServiceImpl add start");
        Professor professor = professorService.getAuthProfessor();
        if (courseDtoForAdd.getId() != null) isCourseAssignedToProfessor(courseDtoForAdd.getId());
        Course course = courseMapper.toEntityForAdd(courseDtoForAdd, this);
        if (courseDtoForAdd.getImage() != null) course.setImage(minioService.putMultipartFile(courseDtoForAdd.getImage()));
        if (professor != null) course.setProfessor(professor);
        long result = save(course).getId();
        log.info("CourseServiceImpl add finish");
        return result;
    }

    @Override
    public void isCourseAssignedToProfessor(long courseId) {
        log.info("CourseServiceImpl isCourseAssignedToProfessor start");
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new EntityNotFoundException("Course with id = " + courseId + " not found")
        );
        if (course.getProfessor() == null || !Objects.equals(course.getProfessor().getId(), professorService.getAuthProfessor().getId()))
            throw new AccessDeniedException("You don't have access to this page");
        log.info("CourseServiceImpl isCourseAssignedToProfessor finish");
    }

    @Override
    public CourseDtoForAdd getByIdForAdd(Long courseId) {
        log.info("CourseServiceImpl getByIdForAdd start");
        CourseDtoForAdd result = courseMapper.toDtoForAdd(getById(courseId));
        log.info("CourseServiceImpl getByIdForAdd finish");
        return result;
    }

    @Override
    public Map<String, String> getForSelect() {
        log.info("CourseServiceImpl getForSelect start");
        Map<String, String> forSelect = new HashMap<>();
        for (Course course : professorService.getAuthProfessor().getCourses()) {
            forSelect.put(course.getId().toString(), course.getName());
        }
        log.info("CourseServiceImpl getForSelect finish");
        return forSelect;
    }

    @Override
    public Map<String, String> getForSelectByStudent(Long studentId) {
        log.info("CourseServiceImpl getForSelectByStudent start");
        Map<String, String> forSelect = new HashMap<>();
        Student student = studentService.getById(studentId);
        for (Course course : professorService.getAuthProfessor().getCourses()) {
            if(student.getCourses().stream().anyMatch(courseStudent -> courseStudent.getId().equals(course.getId())))
                forSelect.put(course.getId().toString(), course.getName());
        }
        log.info("CourseServiceImpl getForSelectByStudent finish");
        return forSelect;
    }

    @Override
    @Transactional
    public void addStudentToCourse(Map<String, String> students, Long courseId) {
        log.info("CourseServiceImpl addStudentToCourse start");
        Course course = getById(courseId);
        students.forEach((studentId, value) -> {
            Student student = studentService.getById(Long.parseLong(studentId));
            if(Boolean.parseBoolean(value)){
                if(student.getCourses().stream().noneMatch(courseStudent -> courseStudent.getId().equals(courseId)))
                    course.getStudents().add(student);
            }
            else {
                if(student.getCourses().stream().anyMatch(courseStudent -> courseStudent.getId().equals(courseId)))
                    course.getStudents().removeIf(studentEl -> studentEl.getId().equals(Long.parseLong(studentId)));
            }
            save(course);
        });
        log.info("CourseServiceImpl addStudentToCourse finish");
    }

    @Override
    @Transactional
    public void removeStudentFromCourse(Long studentId, Long courseId) {
        log.info("CourseServiceImpl removeStudentFromCourse start");
        Course course = getById(courseId);
        Student student = studentService.getById(studentId);
        if(student.getCourses().stream().anyMatch(courseStudent -> courseStudent.getId().equals(courseId)))
            course.getStudents().removeIf(studentEl -> studentEl.getId().equals(studentId));
        save(course);
        log.info("CourseServiceImpl removeStudentFromCourse finish");
    }

    @Override
    public Integer getCountCourse() {
        log.info("CourseServiceImpl getCountCourse start");
        Integer result = professorService.getAuthProfessor().getCourses().size();
        log.info("CourseServiceImpl getCountCourse finish");
        return result;
    }
}
