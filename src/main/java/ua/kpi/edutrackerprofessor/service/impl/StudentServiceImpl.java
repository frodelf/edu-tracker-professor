package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import ua.kpi.edutrackerprofessor.dto.ForSelect2Dto;
import ua.kpi.edutrackerprofessor.dto.student.*;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerprofessor.mapper.StudentMapper;
import ua.kpi.edutrackerprofessor.repository.StudentRepository;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.StudentService;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;
import ua.kpi.edutrackerprofessor.specification.StudentSpecification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerprofessor.specification.StudentSpecificationForStatistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Log4j2
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper = new StudentMapper();
    private final ProfessorService professorService;
    private final StudentsTaskService studentsTaskService;

    @Override
    @Transactional
    public Student save(Student student) {
        log.info("StudentServiceImpl save start");
        Student savedStudent = studentRepository.save(student);
        log.info("StudentServiceImpl save finish");
        return savedStudent;
    }

    @Override
    public long count() {
        log.info("StudentServiceImpl count start");
        long result = studentRepository.count();
        log.info("StudentServiceImpl count finish");
        return result;
    }

    @Override
    public Student getById(long id) {
        log.info("StudentServiceImpl getById start for id: {}", id);
        if(!existsByStudentIdAndProfessorId(id, professorService.getAuthProfessor().getId())){
            log.warn("Access denied for student id: {}", id);
            throw new AccessDeniedException("You don't have access to this page");
        }
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Student with id = "+id+" not found")
        );
        log.info("StudentServiceImpl getById finish for id: {}", id);
        return student;
    }

    @Override
    public Page<StudentResponseViewAll> getAllByCourseList(int page, int pageSize, StudentRequestFilter studentRequestFilter) {
        log.info("StudentServiceImpl getAllByCourseList start");
        List<Course> courseList = professorService.getAuthProfessor().getCourses();
        if(!studentRequestFilter.getGroup().isBlank() || !studentRequestFilter.getFullName().isBlank() || !studentRequestFilter.getTelegram().isBlank() || !studentRequestFilter.getPhone().isBlank() || studentRequestFilter.getCourse() != null){
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id")));
            Specification<Student> spec;
            if(nonNull(studentRequestFilter.getCourse())) spec = new StudentSpecification(studentRequestFilter);
            else spec = new StudentSpecification(studentRequestFilter, courseList);
            return studentMapper.toDtoListForViewAll(studentRepository.findAll(spec, pageable));
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id")));
        Page<StudentResponseViewAll> result =  studentMapper.toDtoListForViewAll(studentRepository.findDistinctByCoursesIn(courseList, pageable));
        log.info("StudentServiceImpl getAllByCourseList finish");
        return result;
    }

    @Override
    public Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto) {
        log.info("StudentServiceImpl getAllByGroupForSelect start");
        Pageable pageable = PageRequest.of(forSelect2Dto.getPage(), forSelect2Dto.getSize(), Sort.by(Sort.Order.desc("id")));
        Page<String> groups = studentRepository.findAllGroupNamesByGroupNameLikeIgnoreCase(forSelect2Dto.getQuery(), pageable);
        List<Map<String, String>> list = new ArrayList<>();
        for (String group : groups.getContent()) {
            Map<String, String> map = new HashMap<>();
            map.put(group, group);
            list.add(map);
        }
        log.info("StudentServiceImpl getAllByGroupForSelect finish");
        return new PageImpl<>(list, pageable, groups.getTotalElements());
    }

    @Override
    public StudentResponseViewOnePage getByIdForView(Long id) {
        log.info("StudentServiceImpl getByIdForView start");
        StudentResponseViewOnePage response = studentMapper.toDtoForViewOnePage(getById(id));
        log.info("StudentServiceImpl getByIdForView finish");
        return response;
    }

    @Override
    public List<StudentResponseForAdd> getAllByGroupAndCourse(String group, Long courseId) {
        log.info("StudentServiceImpl getAllByGroupAndCourse start");
        List<StudentResponseForAdd> result = studentMapper.toDtoForAddList(getAllByGroup(group), courseId);
        log.info("StudentServiceImpl getAllByGroupAndCourse finish");
        return result;
    }

    @Override
    public List<Student> getAllByGroup(String group) {
        log.info("StudentServiceImpl getAllByGroup start");
        List<Student> result = studentRepository.findAllByGroupName(group);
        log.info("StudentServiceImpl getAllByGroup finish");
        return result;
    }

    @Override
    public Map<String, String> getAllGroupByCourseId(Long courseId) {
        log.info("StudentServiceImpl getAllGroupByCourseId start");
        List<String> groups = studentRepository.findAllGroupNamesByCourseId(courseId);
        Map<String, String> list = new HashMap<>();
        for (String group : groups) {
            list.put(group, group);
        }
        log.info("StudentServiceImpl getAllGroupByCourseId finish");
        return list;
    }

    @Override
    public Long getCountActiveStudent() {
        log.info("StudentServiceImpl getCountActiveStudent start");
        long count = studentRepository.countAllByCoursesIn(professorService.getAuthProfessor().getCourses());
        log.info("StudentServiceImpl getCountActiveStudent finish");
        return count;
    }

    @Override
    public List<Student> getAllByCourseId(Long courseId) {
        log.info("StudentServiceImpl getAllByCourseId start");
        List<Student> result = studentRepository.findAllByCourseId(courseId);
        log.info("StudentServiceImpl getAllByCourseId finish");
        return result;
    }

    @Override
    public Page<StudentResponseForStatistic> getAllForStatistic(StudentRequestFilterForStatistic studentRequestFilterForStatistic) {
        log.info("StudentServiceImpl getAllForStatistic start");
        Specification specification = new StudentSpecificationForStatistic(studentRequestFilterForStatistic, professorService.getAuthProfessor().getCourses());
        Pageable pageable = PageRequest.of(studentRequestFilterForStatistic.getPage(), studentRequestFilterForStatistic.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Page<StudentResponseForStatistic> result = studentMapper.toDtoListForStatistic(studentRepository.findAll(specification, pageable), studentRequestFilterForStatistic.getCourseId(), studentsTaskService);
        log.info("StudentServiceImpl getAllForStatistic finish");
        return result;
    }

    @Override
    public boolean existsByStudentIdAndProfessorId(Long studentId, Long professorId) {
        log.info("StudentServiceImpl existsByStudentIdAndProfessorId start");
        boolean exists = studentRepository.existsByStudentIdAndProfessorId(studentId, professorId);
        log.info("StudentServiceImpl existsByStudentIdAndProfessorId finish");
        return exists;
    }

    @Override
    public List<String> getAllEmailsByGroup(String group) {
        log.info("StudentServiceImpl getAllEmailsByGroup start");
        List<Student> students = studentRepository.findAllByGroupName(group);
        List<String> result = new ArrayList<>();
        for (Student student : students) {
            result.add(student.getEmail());
        }
        log.info("StudentServiceImpl getAllEmailsByGroup finish");
        return result;
    }
}