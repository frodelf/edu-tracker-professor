package ua.kpi.edutrackerprofessor.mapper;

import ua.kpi.edutrackerprofessor.dto.student.StudentResponseForAdd;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseForStatistic;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseViewAll;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseViewOnePage;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {
    public StudentResponseViewAll toDtoForViewAll(Student student) {
        StudentResponseViewAll studentResponseViewAll = new StudentResponseViewAll();
        if(student.getId() != null)studentResponseViewAll.setId(student.getId());
        if(student.getGroupName() != null)studentResponseViewAll.setGroup(student.getGroupName());
        if(student.getLastName() != null)studentResponseViewAll.setLastName(student.getLastName());
        if(student.getName() != null)studentResponseViewAll.setName(student.getName());
        if(student.getMiddleName() != null)studentResponseViewAll.setMiddleName(student.getMiddleName());
        if(student.getTelegram() != null)studentResponseViewAll.setTelegram(student.getTelegram());
        if(student.getTelegram() != null)studentResponseViewAll.setTelegram(student.getTelegram());
        if(student.getPhone() != null)studentResponseViewAll.setPhone(student.getPhone());
        studentResponseViewAll.setCourses(new HashMap<>());
        if(student.getCourses()!=null && !student.getCourses().isEmpty()){
            for (Course course : student.getCourses()) {
                studentResponseViewAll.getCourses().put(course.getId().toString(), course.getName());
            }
        }
        return studentResponseViewAll;
    }
    public Page<StudentResponseViewAll> toDtoListForViewAll(Page<Student> students) {
        return new PageImpl<>(students.getContent().stream()
                .map(this::toDtoForViewAll)
                .collect(Collectors.toList()), students.getPageable(), students.getTotalElements());
    }

    public StudentResponseViewOnePage toDtoForViewOnePage(Student student) {
        StudentResponseViewOnePage studentResponseViewOnePage = new StudentResponseViewOnePage();
        if(student.getId() != null)studentResponseViewOnePage.setId(student.getId());
        if(student.getLastName() != null)studentResponseViewOnePage.setLastName(student.getLastName());
        if(student.getName() != null)studentResponseViewOnePage.setName(student.getName());
        if(student.getMiddleName() != null)studentResponseViewOnePage.setMiddleName(student.getMiddleName());
        if(student.getPhone() != null)studentResponseViewOnePage.setPhone(student.getPhone());
        if(student.getEmail() != null)studentResponseViewOnePage.setEmail(student.getEmail());
        if(student.getTelegram() != null)studentResponseViewOnePage.setTelegram(student.getTelegram());
        if(student.getGroupName() != null)studentResponseViewOnePage.setGroupName(student.getGroupName());
        return studentResponseViewOnePage;
    }

    public List<StudentResponseForAdd> toDtoForAddList(List<Student> allByGroupName, Long courseId) {
        return null;
    }
    public StudentResponseForAdd toDtoForAdd(Student student, Long courseId) {
        StudentResponseForAdd studentResponseForAdd = new StudentResponseForAdd();
        studentResponseForAdd.setId(student.getId());
        studentResponseForAdd.setGroupName(student.getGroupName());
        studentResponseForAdd.setGroupName(student.getGroupName());
        studentResponseForAdd.setFullName(student.getLastName()+" "+student.getName());
        studentResponseForAdd.setPresent(student.getCourses().stream().anyMatch(courseStudent -> courseStudent.getId().equals(courseId)));
        return studentResponseForAdd;
    }

    public Page<StudentResponseForStatistic> toDtoListForStatistic(Page<Student> students, Long courseId, StudentsTaskService studentsTaskService) {
        return new PageImpl<>(students.getContent().stream()
                .map(student -> toDtoForStatistic(student, courseId, studentsTaskService))
                .collect(Collectors.toList()), students.getPageable(), students.getTotalElements());
    }
    private StudentResponseForStatistic toDtoForStatistic(Student student, Long courseId, StudentsTaskService studentsTaskService) {
        StudentResponseForStatistic studentResponseForStatistic = new StudentResponseForStatistic();
        studentResponseForStatistic.setId(student.getId());
        studentResponseForStatistic.setGroupName(student.getGroupName());
        studentResponseForStatistic.setFullName(student.getLastName() + " " + student.getName() + " " + student.getMiddleName());
        studentResponseForStatistic.setTelegram(student.getTelegram());
        studentResponseForStatistic.setNumberOfTasksNotDone(String.valueOf(studentsTaskService.countAllNotDoneTaskByStudentIdAndCourseId(student.getId(), courseId)));
        studentResponseForStatistic.setMark(String.valueOf(studentsTaskService.countMarkByStudentIdAndCourseId(student.getId(), courseId)));
        return studentResponseForStatistic;
    }
}
