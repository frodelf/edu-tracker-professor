package ua.kpi.edutrackerprofessor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.LessonService;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {
    @Mock
    private Model model;
    @Mock
    private LessonService lessonService;
    @InjectMocks
    private LessonController lessonController;

    @Test
    void index() {
        ModelAndView modelAndView = lessonController.index();

        assertThat(modelAndView.getViewName()).isEqualTo("lesson/index");
    }

    @Test
    void getStatusForSelect() {
        Map<String, String> mockStatus = Map.of("ACTIVE", "Active", "INACTIVE", "Inactive");

        when(lessonService.getStatusForSelect()).thenReturn(mockStatus);

        ResponseEntity<Map<String, String>> response = lessonController.getStatusForSelect();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockStatus);
    }

    @Test
    void startLesson() {
        LessonRequestForStart lessonRequest = new LessonRequestForStart();

        when(lessonService.start(lessonRequest)).thenReturn(1L);

        ResponseEntity<Long> response = lessonController.startLesson(lessonRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(1L);
    }

    @Test
    void getAllLessons() {
        LessonRequestForFilter filter = new LessonRequestForFilter();
        Page<LessonResponseForViewAll> mockPage = mock(Page.class);

        when(lessonService.getAll(filter)).thenReturn(mockPage);

        ResponseEntity<Page<LessonResponseForViewAll>> response = lessonController.getAllLessons(filter);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockPage);
    }

    @Test
    void editLesson() {
        Long lessonId = 1L;

        ModelAndView modelAndView = lessonController.editLesson(lessonId);

        assertThat(modelAndView.getModel().get("lessonId")).isEqualTo(lessonId);
        assertThat(modelAndView.getViewName()).isEqualTo("lesson/add");
    }

    @Test
    void viewLesson() {
        Long lessonId = 1L;

        ModelAndView modelAndView = lessonController.viewLesson(lessonId);

        assertThat(modelAndView.getModel().get("lessonId")).isEqualTo(lessonId);
        assertThat(modelAndView.getModel().get("view")).isEqualTo(true);
        assertThat(modelAndView.getViewName()).isEqualTo("lesson/add");
    }

    @Test
    void finish() {
        Long lessonId = 1L;

        ResponseEntity<String> response = lessonController.finish(lessonId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("finished");

        verify(lessonService, times(1)).finish(lessonId);
    }

    @Test
    void getCountLessonHeld() {
        when(lessonService.countAllByStatus(any())).thenReturn(10L);

        ResponseEntity<Long> response = lessonController.getCountLessonHeld();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(10L);
    }

    @Test
    void getDateCountMap() {
        Map<String, String> mockMap = Map.of("2025-01-01", "2", "2025-01-02", "3");
        Long courseId = 1L;

        when(lessonService.getDateCountMap(courseId)).thenReturn(mockMap);

        ResponseEntity<Map<String, String>> response = lessonController.getDateCountMap(courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockMap);
    }

    @Test
    void activeMenuItem() {
        lessonController.activeMenuItem(model);

        verify(model).addAttribute("lessonActive", true);
    }
}