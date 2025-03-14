package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerentity.entity.enums.StatusLesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.LessonService;

import java.util.Map;

@Controller
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("lesson/index");
    }
    @GetMapping("/get-status-for-select")
    public ResponseEntity<Map<String, String>> getStatusForSelect() {
        return ResponseEntity.ok(lessonService.getStatusForSelect());
    }
    @PostMapping("/start")
    public ResponseEntity<Long> startLesson(@ModelAttribute @Valid LessonRequestForStart lessonRequestForStart) {
        return ResponseEntity.ok(lessonService.start(lessonRequestForStart));
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<LessonResponseForViewAll>> getAllLessons(@ModelAttribute @Valid LessonRequestForFilter lessonRequestForFilter) {
        return ResponseEntity.ok(lessonService.getAll(lessonRequestForFilter));
    }
    @GetMapping("/edit/{id}")
    public ModelAndView editLesson(@PathVariable Long id) {
        return new ModelAndView("lesson/add", "lessonId", id);
    }
    @GetMapping("/view/{id}")
    public ModelAndView viewLesson(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("lesson/add");
        modelAndView.addObject("lessonId", id);
        modelAndView.addObject("view", true);
        return modelAndView;
    }
    @PostMapping("/finish")
    public ResponseEntity<String> finish(@RequestParam Long lessonId){
        lessonService.finish(lessonId);
        return ResponseEntity.ok("finished");
    }
    @GetMapping("/get-count-lesson-held")
    public ResponseEntity<Long> getCountLessonHeld(){
        return ResponseEntity.ok(lessonService.countAllByStatus(StatusLesson.FINISHED));
    }
    @GetMapping("/get-date-count-map")
    public ResponseEntity<Map<String, String>> getDateCountMap(@RequestParam(required = false) Long courseId){
        Map<String, String> res = lessonService.getDateCountMap(courseId);
        return ResponseEntity.ok(res);
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("lessonActive", true);
    }
}