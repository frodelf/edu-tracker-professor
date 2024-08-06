package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.ForSelect2Dto;
import ua.kpi.edutrackerprofessor.dto.student.*;
import ua.kpi.edutrackerprofessor.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final StatisticService statisticService;

    @GetMapping({"", "/"})
    public ModelAndView index() {
        return new ModelAndView("student/index");
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable Long id) {
        return new ModelAndView("student/view", "student", studentService.getByIdForView(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<StudentResponseViewAll>> getAll(@ModelAttribute @Valid StudentRequestFilter studentRequestFilter) {
        return ResponseEntity.ok(studentService.getAllByCourseList(studentRequestFilter.getPage(), studentRequestFilter.getPageSize(), studentRequestFilter));
    }

    @GetMapping("/get-group-for-select")
    public ResponseEntity<Page<Map<String, String>>> getGroupForSelect(@ModelAttribute ForSelect2Dto forSelect2Dto) {
        return ResponseEntity.ok(studentService.getAllByGroupForSelect(forSelect2Dto));
    }
    @GetMapping("/get-groups-by-course-for-select/{courseId}")
    public ResponseEntity<Map<String, String>> getGroupsByCourseForSelect(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.getAllGroupByCourseId(courseId));
    }
    @GetMapping("/statistic")
    public ResponseEntity<Map<String, String>> statistic(@RequestParam(required = false) Long courseId, @RequestParam Long studentId) {
        return ResponseEntity.ok(statisticService.getStatisticForStudent(courseId, studentId));
    }

    @GetMapping("/get-all-by-group-and-course")
    public ResponseEntity<List<StudentResponseForAdd>> getAllByGroupAndCourse(@RequestParam String group, @RequestParam Long courseId) {
        return ResponseEntity.ok(studentService.getAllByGroupAndCourse(group, courseId));
    }
    @GetMapping("/get-all-for-statistic")
    public ResponseEntity<Page<StudentResponseForStatistic>> getAllForStatistic(@ModelAttribute @Valid StudentRequestFilterForStatistic studentRequestFilterForStatistic) {
        return ResponseEntity.ok(studentService.getAllForStatistic(studentRequestFilterForStatistic));
    }
    @PostMapping("/add-to-course")
    public ResponseEntity<String> addToCourse(@RequestParam Map<String, String> students, @RequestParam Long courseId) {
        students.remove("courseId");
        courseService.addStudentToCourse(students, courseId);
        return ResponseEntity.ok("saved");
    }

    @DeleteMapping("/delete-from-course")
    public ResponseEntity<String> deleteFromCourse(@RequestParam Long studentId, @RequestParam Long courseId) {
        courseService.removeStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok("deleted");
    }
    @GetMapping("/get-count-active-student")
    public ResponseEntity<Long> getCountActiveStudent(){
        return ResponseEntity.ok(studentService.getCountActiveStudent());
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("studentActive", true);
    }
}