package ua.kpi.edutrackerprofessor.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerprofessor.service.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentsTaskService studentsTaskService;
    private final ReviewService reviewService;
    @Override
    public Map<String, String> getStatisticForStudent(Long courseId, Long studentId) {
        Map<String, String> map = new HashMap<>();
        Student student;
        Course course;
        if(nonNull(courseId)) {
            course = courseService.getById(courseId);
        }else {
            student = studentService.getById(studentId);
            if(student.getCourses()!=null && !student.getCourses().isEmpty())course = student.getCourses().get(0);
            else return Collections.singletonMap("status", "no data");
        }
        map.put("course", course.getName());
        map.put("courseId", String.valueOf(course.getId()));
        map.put("allTasks", String.valueOf(studentsTaskService.countAllByStudentIdAndCourseId(studentId, courseId)));
        map.put("doneTasks", String.valueOf(studentsTaskService.countAllDoneTaskByStudentIdAndCourseId(studentId, courseId)));
        map.put("notDoneTasks", String.valueOf(studentsTaskService.countAllNotDoneTaskByStudentIdAndCourseId(studentId, courseId)));
        map.put("lessons", String.valueOf(reviewService.countAllVisitedLessonByStudentIdAndCourseId(studentId, courseId)));
        map.put("mark", String.valueOf(studentsTaskService.countMarkByStudentIdAndCourseId(studentId, courseId)));
        return map;
    }
}