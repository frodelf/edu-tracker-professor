package ua.kpi.edutrackerprofessor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatisticControllerTest {
    @InjectMocks
    private StatisticController statisticController;
    @Mock
    private Model model;

    @Test
    void index() {
        ModelAndView modelAndView = statisticController.index();

        assertThat(modelAndView.getViewName()).isEqualTo("statistic/index");
    }

    @Test
    void activeMenuItem() {
        statisticController.activeMenuItem(model);

        verify(model, times(1)).addAttribute("statisticActive", true);
    }
}
