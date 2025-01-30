package ua.kpi.edutrackerprofessor.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorRequestForPersonalData;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForPersonalData;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.validation.ContactValidator;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProfessorControllerTest {
    @Mock
    private ProfessorService professorService;
    @Mock
    private ContactValidator contactValidator;
    @InjectMocks
    private ProfessorController professorController;
    @Mock
    private BindingResult bindingResult;

    @Test
    void getFirstCourseWhenCoursesExist() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Math 101");
        Professor professor = new Professor();
        professor.setCourses(Collections.singletonList(course));

        when(professorService.getAuthProfessor()).thenReturn(professor);

        ResponseEntity<Map<String, String>> response = professorController.getFirstCourse();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("1", "Math 101");
    }

    @Test
    void getFirstCourseWhenNoCoursesExist() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Math 101");
        Professor professor = new Professor();
        professor.setCourses(Collections.emptyList());

        when(professorService.getAuthProfessor()).thenReturn(professor);

        assertThatThrownBy(() -> professorController.getFirstCourse())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("There are no courses in auth professor");
    }

    @Test
    void getPersonalData() {
        ModelAndView modelAndView = professorController.getPersonalData();

        assertThat(modelAndView.getViewName()).isEqualTo("professor/personal-data");
    }

    @Test
    void updatePersonalDataWhenValidData() throws NoSuchMethodException, MethodArgumentNotValidException {
        when(bindingResult.hasErrors()).thenReturn(false);
        ProfessorRequestForPersonalData personalData = mock(ProfessorRequestForPersonalData.class);

        ResponseEntity<String> response = professorController.updatePersonalData(personalData, bindingResult);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("saved");
        verify(professorService, times(1)).updatePersonalData(personalData);
    }

    @Test
    void updatePersonalDataWhenValidationFails() {
        when(bindingResult.hasErrors()).thenReturn(true);
        ProfessorRequestForPersonalData personalData = mock(ProfessorRequestForPersonalData.class);

        assertThatThrownBy(() -> professorController.updatePersonalData(personalData, bindingResult))
                .isInstanceOf(MethodArgumentNotValidException.class);
    }

    @Test
    void getAuthProfessor() {
        ProfessorResponseForPersonalData professorResponse = new ProfessorResponseForPersonalData();
        when(professorService.getAuthProfessorForPersonalData()).thenReturn(professorResponse);

        ResponseEntity<ProfessorResponseForPersonalData> response = professorController.getAuthProfessor();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(professorResponse);
    }
}
