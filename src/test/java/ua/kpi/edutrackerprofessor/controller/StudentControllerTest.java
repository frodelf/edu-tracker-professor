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
import ua.kpi.edutrackerprofessor.dto.ForSelect2Dto;
import ua.kpi.edutrackerprofessor.dto.student.*;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.StatisticService;
import ua.kpi.edutrackerprofessor.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @InjectMocks
    private StudentController studentController;
    @Mock
    private StudentService studentService;
    @Mock
    private StatisticService statisticService;
    @Mock
    private CourseService courseService;
    @Mock
    private Model model;

    @Test
    void index() {
        ModelAndView modelAndView = studentController.index();

        assertThat(modelAndView.getViewName()).isEqualTo("student/index");
    }

    @Test
    void getById() {
        Long studentId = 1L;
        StudentResponseViewOnePage studentResponse = mock(StudentResponseViewOnePage.class);
        when(studentService.getByIdForView(studentId)).thenReturn(studentResponse);

        ModelAndView modelAndView = studentController.getById(studentId);

        assertThat(modelAndView.getViewName()).isEqualTo("student/view");
        assertThat(modelAndView.getModel().get("student")).isEqualTo(studentResponse);
    }

    @Test
    void getAll() {
        StudentRequestFilter filter = new StudentRequestFilter();
        filter.setPage(0);
        filter.setPageSize(10);
        Page<StudentResponseViewAll> studentResponseViewAllPage = mock(Page.class);
        when(studentService.getAllByCourseList(0, 10, filter)).thenReturn(studentResponseViewAllPage);

        ResponseEntity<Page<StudentResponseViewAll>> response = studentController.getAll(filter);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void getGroupForSelect() {
        ForSelect2Dto forSelect2Dto = new ForSelect2Dto();
        forSelect2Dto.setPage(1);
        forSelect2Dto.setSize(10);
        Page<Map<String, String>> mockResponse = Page.empty();

        when(studentService.getAllByGroupForSelect(forSelect2Dto)).thenReturn(mockResponse);

        ResponseEntity<Page<Map<String, String>>> response = studentController.getGroupForSelect(forSelect2Dto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(studentService, times(1)).getAllByGroupForSelect(forSelect2Dto);
    }

    @Test
    void getGroupsByCourseForSelect() {
        Long courseId = 1L;
        Map<String, String> mockResponse = Map.of("group1", "Group 1", "group2", "Group 2");

        when(studentService.getAllGroupByCourseId(courseId)).thenReturn(mockResponse);

        ResponseEntity<Map<String, String>> response = studentController.getGroupsByCourseForSelect(courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(studentService, times(1)).getAllGroupByCourseId(courseId);
    }

    @Test
    void statistic() {
        Long courseId = 1L;
        Long studentId = 1L;
        when(statisticService.getStatisticForStudent(courseId, studentId)).thenReturn(Map.of("key", "value"));

        ResponseEntity<Map<String, String>> response = studentController.statistic(courseId, studentId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void getAllByGroupAndCourse() {
        String group = "Group A";
        Long courseId = 1L;
        when(studentService.getAllByGroupAndCourse(group, courseId)).thenReturn(List.of(new StudentResponseForAdd()));

        ResponseEntity<List<StudentResponseForAdd>> response = studentController.getAllByGroupAndCourse(group, courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();

        verify(studentService, times(1)).getAllByGroupAndCourse(group, courseId);
    }

    @Test
    void getAllForStatistic() {
        StudentRequestFilterForStatistic filterForStatistic = new StudentRequestFilterForStatistic();
        when(studentService.getAllForStatistic(filterForStatistic)).thenReturn(Page.empty());

        ResponseEntity<Page<StudentResponseForStatistic>> response = studentController.getAllForStatistic(filterForStatistic);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void addToCourse() {
        Map<String, String> students = new HashMap<>();
        students.put("student1", "John Doe");
        students.put("student2", "Jane Smith");
        Long courseId = 1L;
        doNothing().when(courseService).addStudentToCourse(students, courseId);

        ResponseEntity<String> response = studentController.addToCourse(students, courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("saved");

        verify(courseService, times(1)).addStudentToCourse(students, courseId);
    }

    @Test
    void deleteFromCourse() {
        Long studentId = 1L;
        Long courseId = 1L;
        doNothing().when(courseService).removeStudentFromCourse(studentId, courseId);

        ResponseEntity<String> response = studentController.deleteFromCourse(studentId, courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("deleted");

        verify(courseService, times(1)).removeStudentFromCourse(studentId, courseId);
    }

    @Test
    void getCountActiveStudent() {
        when(studentService.getCountActiveStudent()).thenReturn(5L);

        ResponseEntity<Long> response = studentController.getCountActiveStudent();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(5L);

        verify(studentService, times(1)).getCountActiveStudent();
    }

    @Test
    void activeMenuItem() {
        studentController.activeMenuItem(model);

        verify(model, times(1)).addAttribute("studentActive", true);
    }
}