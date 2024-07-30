package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
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
    public ResponseEntity<Long> startLesson(@ModelAttribute LessonRequestForStart lessonRequestForStart) {
        return ResponseEntity.ok(lessonService.start(lessonRequestForStart));
    }
    //TODO зупинився на тому що не зміг отримати дату в дто
    @GetMapping("/get-all")
    public ResponseEntity<Page<LessonResponseForViewAll>> getAllLessons(@ModelAttribute @Valid LessonRequestForFilter lessonRequestForFilter) {
        return ResponseEntity.ok(lessonService.getAll(lessonRequestForFilter));
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("lessonActive", true);
    }
}