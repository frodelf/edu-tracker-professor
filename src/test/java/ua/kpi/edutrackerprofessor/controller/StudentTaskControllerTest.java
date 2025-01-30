package ua.kpi.edutrackerprofessor.controller;

import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForLessonEdit;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentTaskControllerTest {
    @Mock
    private StudentsTaskService studentsTaskService;
    @InjectMocks
    private StudentTaskController studentTaskController;

    @Test
    void getAllStudentByTask() {
        StudentTaskRequestForFilter filter = new StudentTaskRequestForFilter();
        filter.setPage(1);
        filter.setPageSize(10);
        Page<StudentTaskResponseForViewAll> mockResponse = Page.empty();

        when(studentsTaskService.getAll(filter)).thenReturn(mockResponse);

        ResponseEntity<Page<StudentTaskResponseForViewAll>> response = studentTaskController.getAllStudentByTask(filter);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(studentsTaskService, times(1)).getAll(filter);
    }

    @Test
    void cancelMark() throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Long studentTaskId = 1L;

        doNothing().when(studentsTaskService).cancelMark(studentTaskId);

        ResponseEntity<String> response = studentTaskController.cancelMark(studentTaskId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("updated");

        verify(studentsTaskService, times(1)).cancelMark(studentTaskId);
    }

    @Test
    void evaluate() {
        Long studentTaskId = 1L;
        Double mark = 5.0;

        doNothing().when(studentsTaskService).evaluate(studentTaskId, mark);

        ResponseEntity<String> response = studentTaskController.evaluate(studentTaskId, mark);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("updated");

        verify(studentsTaskService, times(1)).evaluate(studentTaskId, mark);
    }

    @Test
    void getAllForLessonEdit() {
        Long studentId = 1L;
        Long lessonId = 1L;
        List<StudentTaskResponseForLessonEdit> mockResponse = List.of(new StudentTaskResponseForLessonEdit());

        when(studentsTaskService.getAllByStudentIdAndLessonId(studentId, lessonId)).thenReturn(mockResponse);

        ResponseEntity<List<StudentTaskResponseForLessonEdit>> response = studentTaskController.getAllForLessonEdit(studentId, lessonId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);

        verify(studentsTaskService, times(1)).getAllByStudentIdAndLessonId(studentId, lessonId);
    }
}