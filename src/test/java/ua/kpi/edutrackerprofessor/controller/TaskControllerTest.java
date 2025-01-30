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
import ua.kpi.edutrackerprofessor.dto.task.*;
import ua.kpi.edutrackerprofessor.service.TaskService;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;
    @Mock
    private TaskService taskService;

    @Test
    void index() {
        ModelAndView modelAndView = taskController.index();

        assertThat(modelAndView.getViewName()).isEqualTo("task/index");
    }

    @Test
    void viewPage() {
        Long taskId = 1L;
        TaskResponseForView mockTaskResponse = mock(TaskResponseForView.class);
        when(taskService.getByIdForView(taskId)).thenReturn(mockTaskResponse);

        ModelAndView modelAndView = taskController.viewPage(taskId);

        assertThat(modelAndView.getViewName()).isEqualTo("task/view");
        assertThat(modelAndView.getModel().get("task")).isEqualTo(mockTaskResponse);

        verify(taskService, times(1)).getByIdForView(taskId);
    }

    @Test
    void getAll() {
        TaskRequestFilter filter = new TaskRequestFilter();
        filter.setPage(1);
        filter.setPageSize(10);
        Page<TaskResponseViewAll> mockResponse = Page.empty();

        when(taskService.getAll(filter)).thenReturn(mockResponse);

        ResponseEntity<Page<TaskResponseViewAll>> response = taskController.getAll(filter);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(taskService, times(1)).getAll(filter);
    }

    @Test
    void delete() {
        Long taskId = 1L;

        doNothing().when(taskService).deleteById(taskId);

        ResponseEntity<String> response = taskController.delete(taskId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("deleted");

        verify(taskService, times(1)).deleteById(taskId);
    }

    @Test
    void getById() {
        Long taskId = 1L;
        TaskResponseForAdd mockResponse = new TaskResponseForAdd();
        when(taskService.getByIdForAdd(taskId)).thenReturn(mockResponse);

        ResponseEntity<TaskResponseForAdd> response = taskController.getById(taskId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(taskService, times(1)).getByIdForAdd(taskId);
    }

    @Test
    void add() throws Exception {
        TaskRequestForAdd taskRequestForAdd = new TaskRequestForAdd();

        doNothing().when(taskService).add(taskRequestForAdd);

        ResponseEntity<String> response = taskController.add(taskRequestForAdd);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("saved");

        verify(taskService, times(1)).add(taskRequestForAdd);
    }

    @Test
    void getAllCloseForSelectByCourseId() {
        Long courseId = 1L;
        Map<String, String> mockResponse = Map.of("1", "Task 1", "2", "Task 2");
        when(taskService.getAllCloseForSelectByCourseId(courseId)).thenReturn(mockResponse);

        ResponseEntity<Map<String, String>> response = taskController.getAllCloseForSelectByCourseId(courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(taskService, times(1)).getAllCloseForSelectByCourseId(courseId);
    }

    @Test
    void getAllOpenForSelectByCourseId() {
        Long courseId = 1L;
        Map<String, String> mockResponse = Map.of("3", "Task 3", "4", "Task 4");
        when(taskService.getAllOpenForSelectByCourseId(courseId)).thenReturn(mockResponse);

        ResponseEntity<Map<String, String>> response = taskController.getAllOpenForSelectByCourseId(courseId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(taskService, times(1)).getAllOpenForSelectByCourseId(courseId);
    }

    @Test
    void open() throws Exception {
        TaskRequestForOpen taskRequestForOpen = new TaskRequestForOpen();

        doNothing().when(taskService).open(taskRequestForOpen);

        ResponseEntity<String> response = taskController.open(taskRequestForOpen);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("opened");

        verify(taskService, times(1)).open(taskRequestForOpen);
    }

    @Test
    void close() {
        List<Long> taskForClose = List.of(1L, 2L);

        doNothing().when(taskService).close(taskForClose);

        ResponseEntity<String> response = taskController.close(taskForClose);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("opened");

        verify(taskService, times(1)).close(taskForClose);
    }

    @Test
    void getCountTask() {
        Long mockCount = 10L;
        when(taskService.countAllTask()).thenReturn(mockCount);

        ResponseEntity<Long> response = taskController.getCountTask();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockCount);

        verify(taskService, times(1)).countAllTask();
    }

    @Test
    void activeMenuItem() {
        Model model = mock(Model.class);

        taskController.activeMenuItem(model);

        verify(model, times(1)).addAttribute("taskActive", true);
    }
}
