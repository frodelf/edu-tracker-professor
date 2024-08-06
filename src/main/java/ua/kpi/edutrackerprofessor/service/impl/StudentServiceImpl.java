package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
        return studentRepository.save(student);
    }
    @Override
    public long count() {
        return studentRepository.count();
    }
    @Override
    public Student getById(long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Student with id = "+id+" not found")
        );
    }
    @Override
    public Page<StudentResponseViewAll> getAllByCourseList(int page, int pageSize, StudentRequestFilter studentRequestFilter) {
        List<Course> courseList = professorService.getAuthProfessor().getCourses();
        if(!studentRequestFilter.getGroup().isBlank() || !studentRequestFilter.getFullName().isBlank() || !studentRequestFilter.getTelegram().isBlank() || !studentRequestFilter.getPhone().isBlank() || studentRequestFilter.getCourse() != null){
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id")));
            Specification<Student> spec;
            if(nonNull(studentRequestFilter.getCourse())) spec = new StudentSpecification(studentRequestFilter);
            else spec = new StudentSpecification(studentRequestFilter, courseList);
            return studentMapper.toDtoListForViewAll(studentRepository.findAll(spec, pageable));
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("id")));
        return studentMapper.toDtoListForViewAll(studentRepository.findDistinctByCoursesIn(courseList, pageable));
    }
    @Override
    public Page<Map<String, String>> getAllByGroupForSelect(ForSelect2Dto forSelect2Dto) {
        Pageable pageable = PageRequest.of(forSelect2Dto.getPage(), forSelect2Dto.getSize(), Sort.by(Sort.Order.desc("id")));
        Page<String> groups = studentRepository.findAllGroupNamesByGroupNameLikeIgnoreCase(forSelect2Dto.getQuery(), pageable);
        List<Map<String, String>> list = new ArrayList<>();
        for (String group : groups.getContent()) {
            Map<String, String> map = new HashMap<>();
            map.put(group, group);
            list.add(map);
        }
        return new PageImpl<>(list, pageable, groups.getTotalElements());
    }
    @Override
    public StudentResponseViewOnePage getByIdForView(Long id) {
        return studentMapper.toDtoForViewOnePage(getById(id));
    }
    @Override
    public List<StudentResponseForAdd> getAllByGroupAndCourse(String group, Long courseId) {
        return studentMapper.toDtoForAddList(getAllByGroup(group), courseId);
    }
    @Override
    public List<Student> getAllByGroup(String group) {
        return studentRepository.findAllByGroupName(group);
    }
    @Override
    public Map<String, String> getAllGroupByCourseId(Long courseId) {
        List<String> groups = studentRepository.findAllGroupNamesByCourseId(courseId);
        Map<String, String> list = new HashMap<>();
        for (String group : groups) {
            list.put(group, group);
        }
        return list;
    }
    @Override
    public Long getCountActiveStudent() {
        return studentRepository.countAllByCoursesIn(professorService.getAuthProfessor().getCourses());
    }
    @Override
    public List<Student> getAllByCourseId(Long courseId) {
        return studentRepository.findAllByCourseId(courseId);
    }

    @Override
    public Page<StudentResponseForStatistic> getAllForStatistic(StudentRequestFilterForStatistic studentRequestFilterForStatistic) {
        Specification specification = new StudentSpecificationForStatistic(studentRequestFilterForStatistic, professorService.getAuthProfessor().getCourses());
        Pageable pageable = PageRequest.of(studentRequestFilterForStatistic.getPage(), studentRequestFilterForStatistic.getPageSize(), Sort.by(Sort.Order.desc("id")));
        return studentMapper.toDtoListForStatistic(studentRepository.findAll(specification, pageable), studentRequestFilterForStatistic.getCourseId(), studentsTaskService);
    }
}