package ua.kpi.edutrackerprofessor.controller;

import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.LessonService;
import ua.kpi.edutrackerprofessor.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final LessonService lessonService;
    private final TaskService taskService;
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("course/index");
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<CourseResponseViewAll>> getAll(@RequestParam int page, @RequestParam int pageSize){
        return ResponseEntity.ok(courseService.getAll(page, pageSize));
    }
    @GetMapping("/statistic")
    public ResponseEntity<Map<String, String>> statistic(@RequestParam long id){
        Map<String, String> statistic = new HashMap<>();
        Course course = courseService.getById(id);

        if(course.getStudents() != null)statistic.put("students", String.valueOf(course.getStudents().size()));
        else statistic.put("students", "0");
        if(course.getLiteratures() != null)statistic.put("literatures", String.valueOf(course.getLiteratures().size()));
        else statistic.put("literatures", "0");
        statistic.put("lessons", String.valueOf(lessonService.countByCourseId(course.getId())));
        statistic.put("allTasks", String.valueOf(taskService.countAllTasksByCourseId(course.getId())));
        statistic.put("openTasks", String.valueOf(taskService.countAllOpenTasksByCourseId(course.getId())));
        statistic.put("closeTasks", String.valueOf(taskService.countAllCloseTasksByCourseId(course.getId())));

        return ResponseEntity.ok(statistic);
    }
    @GetMapping("/get-for-select")
    public ResponseEntity<Map<String, String>> getForSelect(){
        return ResponseEntity.ok(courseService.getForSelect());
    }
    @GetMapping("/get-for-select-by-student/{studentId}")
    public ResponseEntity<Map<String, String>> getForSelect(@PathVariable Long studentId){
        return ResponseEntity.ok(courseService.getForSelectByStudent(studentId));
    }
    @GetMapping("/get-count-course")
    public ResponseEntity<Integer> getCountCourse(){
        return ResponseEntity.ok(courseService.getCountCourse());
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("courseActive", true);
    }
}