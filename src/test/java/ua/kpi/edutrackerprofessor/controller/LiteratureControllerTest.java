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
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.LiteratureService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LiteratureControllerTest {

    @Mock
    private LiteratureService literatureService;

    @Mock
    private Model model;

    @InjectMocks
    private LiteratureController literatureController;

    @Test
    void index() {
        ModelAndView result = literatureController.index();

        assertThat(result.getViewName()).isEqualTo("literature/index");
    }

    @Test
    void getAll() {
        LiteratureRequestForFilter filter = new LiteratureRequestForFilter();
        Page<LiteratureResponseForViewAll> page = mock(Page.class);
        when(literatureService.getAll(filter)).thenReturn(page);

        ResponseEntity<Page<LiteratureResponseForViewAll>> result = literatureController.getAll(filter);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(page);
    }

    @Test
    void getById() {
        Long id = 1L;
        LiteratureResponseForAdd literatureResponse = new LiteratureResponseForAdd();
        when(literatureService.getByIdForAdd(id)).thenReturn(literatureResponse);

        ResponseEntity<LiteratureResponseForAdd> result = literatureController.getById(id);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(literatureResponse);
    }

    @Test
    void add() {
        LiteratureRequestForAdd literatureRequest = new LiteratureRequestForAdd();
        Long id = 1L;
        when(literatureService.add(literatureRequest)).thenReturn(id);

        ResponseEntity<Long> result = literatureController.add(literatureRequest);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(id);
    }

    @Test
    void delete() {
        Long id = 1L;

        ResponseEntity<String> result = literatureController.delete(id);

        verify(literatureService).deleteById(id);
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("deleted");
    }

    @Test
    void activeMenuItem() {
        literatureController.activeMenuItem(model);

        verify(model).addAttribute("literatureActive", true);
    }
}
