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
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerprofessor.dto.course.CourseResponseViewAll;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.LessonService;
import ua.kpi.edutrackerprofessor.service.TaskService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private LessonService lessonService;

    @Mock
    private TaskService taskService;

    @Mock
    private Model model;

    @InjectMocks
    private CourseController courseController;

    @Test
    void index() {
        ModelAndView result = courseController.index();
        assertEquals("course/index", result.getViewName());
    }

    @Test
    void getAll() {
        int page = 1;
        int pageSize = 10;
        Page<CourseResponseViewAll> mockPage = mock(Page.class);
        when(courseService.getAll(page, pageSize)).thenReturn(mockPage);

        ResponseEntity<Page<CourseResponseViewAll>> result = courseController.getAll(page, pageSize);

        assertEquals(mockPage, result.getBody());
        verify(courseService, times(1)).getAll(page, pageSize);
    }

    @Test
    void statistic() {
        long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setStudents(null);
        course.setLiteratures(null);

        when(courseService.getById(courseId)).thenReturn(course);
        when(lessonService.countByCourseId(courseId)).thenReturn(5L);
        when(taskService.countAllTasksByCourseId(courseId)).thenReturn(10L);
        when(taskService.countAllOpenTasksByCourseId(courseId)).thenReturn(4L);
        when(taskService.countAllCloseTasksByCourseId(courseId)).thenReturn(6L);

        ResponseEntity<Map<String, String>> result = courseController.statistic(courseId);

        Map<String, String> expected = new HashMap<>();
        expected.put("students", "0");
        expected.put("literatures", "0");
        expected.put("lessons", "5");
        expected.put("allTasks", "10");
        expected.put("openTasks", "4");
        expected.put("closeTasks", "6");

        assertEquals(expected, result.getBody());
        verify(courseService, times(1)).getById(courseId);
        verify(lessonService, times(1)).countByCourseId(courseId);
        verify(taskService, times(1)).countAllTasksByCourseId(courseId);
    }

    @Test
    void getForSelect() {
        Map<String, String> mockSelection = new HashMap<>();
        when(courseService.getForSelect()).thenReturn(mockSelection);

        ResponseEntity<Map<String, String>> result = courseController.getForSelect();

        assertEquals(mockSelection, result.getBody());
        verify(courseService, times(1)).getForSelect();
    }

    @Test
    void getForSelectByStudent() {
        Long studentId = 1L;
        Map<String, String> mockSelection = new HashMap<>();
        when(courseService.getForSelectByStudent(studentId)).thenReturn(mockSelection);

        ResponseEntity<Map<String, String>> result = courseController.getForSelect(studentId);

        assertEquals(mockSelection, result.getBody());
        verify(courseService, times(1)).getForSelectByStudent(studentId);
    }

    @Test
    void getCountCourse() {
        int courseCount = 42;
        when(courseService.getCountCourse()).thenReturn(courseCount);

        ResponseEntity<Integer> result = courseController.getCountCourse();

        assertEquals(courseCount, result.getBody());
        verify(courseService, times(1)).getCountCourse();
    }

    @Test
    void activeMenuItem() {
        courseController.activeMenuItem(model);
        verify(model, times(1)).addAttribute("courseActive", true);
    }
}
