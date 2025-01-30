package ua.kpi.edutrackerprofessor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorResponseForGlobal;
import ua.kpi.edutrackerprofessor.service.ProfessorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private GlobalController globalController;

    @Test
    void globalTeamLeadAttribute() {
        ProfessorResponseForGlobal expectedProfessor = new ProfessorResponseForGlobal();
        expectedProfessor.setId(1L);
        expectedProfessor.setName("Prof. Test");

        when(professorService.getAuthProfessorForGlobal()).thenReturn(expectedProfessor);

        ProfessorResponseForGlobal result = globalController.globalTeamLeadAttribute();

        assertNotNull(result);
        assertEquals(expectedProfessor.getId(), result.getId());
        assertEquals(expectedProfessor.getName(), result.getName());
    }
}
