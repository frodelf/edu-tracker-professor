package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.ForSelect2Dto;
import ua.kpi.edutrackerprofessor.dto.student.StudentRequestFilter;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseForAdd;
import ua.kpi.edutrackerprofessor.dto.student.StudentResponseViewAll;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.ReviewService;
import ua.kpi.edutrackerprofessor.service.StudentService;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentsTaskService studentsTaskService;
    private final ReviewService reviewService;
    @GetMapping({"", "/"})
    public ModelAndView index() {
        return new ModelAndView("student/index");
    }
    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable Long id){
        return new ModelAndView("student/view", "student", studentService.getByIdForView(id));
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<StudentResponseViewAll>> getAll(@ModelAttribute @Valid StudentRequestFilter studentRequestFilter){
        return ResponseEntity.ok(studentService.getAllByCourseList(studentRequestFilter.getPage(), studentRequestFilter.getPageSize(), studentRequestFilter));
    }
    @GetMapping("/get-group-for-select")
    public ResponseEntity<Page<Map<String, String>>> getGroupForSelect(@ModelAttribute ForSelect2Dto forSelect2Dto){
        return ResponseEntity.ok(studentService.getAllByGroupForSelect(forSelect2Dto));
    }
    @GetMapping("/statistic")
    public ResponseEntity<Map<String, String>> statistic(@RequestParam(required = false) Long courseId, @RequestParam Long studentId){
        Map<String, String> map = new HashMap<>();
        Student student = studentService.getById(studentId);
        if(nonNull(courseId)){
            Course course = courseService.getById(courseId);
            map.put("courses", course.getName());
            map.put("allTasks", String.valueOf(studentsTaskService.countAllByStudentIdAndCourseId(studentId, courseId)));
            map.put("doneTasks", String.valueOf(studentsTaskService.countAllDoneTaskByStudentIdAndCourseId(studentId, courseId)));
            map.put("notDoneTasks", String.valueOf(studentsTaskService.countAllNotDoneTaskByStudentIdAndCourseId(studentId, courseId)));
            map.put("lessons", String.valueOf(reviewService.countAllVisitedLessonByStudentIdAndCourseId(studentId, courseId)));
            map.put("mark", String.valueOf(studentsTaskService.countMarkByStudentIdAndCourseId(studentId, courseId)));
        }else {
            map.put("courses", String.valueOf(student.getCourses().size()));
            map.put("allTasks", String.valueOf(studentsTaskService.countAllByStudentId(studentId)));
            map.put("doneTasks", String.valueOf(studentsTaskService.countAllDoneTaskByStudentId(studentId)));
            map.put("notDoneTasks", String.valueOf(studentsTaskService.countAllNotDoneTaskByStudentId(studentId)));
            map.put("lessons", String.valueOf(reviewService.countAllVisitedLessonByStudentId(studentId)));
        }
        map.put("withCourse", String.valueOf(nonNull(courseId)));
        return ResponseEntity.ok(map);
    }
    @GetMapping("/get-all-by-group-and-course")
    public ResponseEntity<List<StudentResponseForAdd>> getAllByGroupAndCourse(@RequestParam String group, @RequestParam Long courseId){
        return ResponseEntity.ok(studentService.getAllByGroupAndCourse(group, courseId));
    }
    @PostMapping("/add-to-course")
    public ResponseEntity<String> addToCourse(@RequestParam Map<String, String> students, @RequestParam Long courseId){
        students.remove("courseId");
        courseService.addStudentToCourse(students, courseId);
        return ResponseEntity.ok("saved");
    }
    @DeleteMapping("/delete-from-course")
    public ResponseEntity<String> deleteFromCourse(@RequestParam Long studentId, @RequestParam Long courseId){
        courseService.removeStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok("deleted");
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("studentActive", true);
    }
}