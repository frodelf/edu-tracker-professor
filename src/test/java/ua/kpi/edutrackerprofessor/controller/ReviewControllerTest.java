package ua.kpi.edutrackerprofessor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ua.kpi.edutrackerprofessor.dto.review.ReviewDtoForEdit;
import ua.kpi.edutrackerprofessor.dto.review.ReviewRequestForFilter;
import ua.kpi.edutrackerprofessor.service.ReviewService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void getAllForLessonEdit() {
        ReviewRequestForFilter filter = new ReviewRequestForFilter();
        Page<ReviewDtoForEdit> page = mock(Page.class);
        when(reviewService.getAllForLessonEdit(filter)).thenReturn(page);

        ResponseEntity<Page<ReviewDtoForEdit>> response = reviewController.getAllForLessonEdit(filter);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(page);
        verify(reviewService, times(1)).getAllForLessonEdit(filter);
    }

    @Test
    void updatePresent() {
        Long reviewId = 1L;
        Boolean checked = true;

        ResponseEntity<String> response = reviewController.updatePresent(reviewId, checked);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("updated");
        verify(reviewService, times(1)).updatePresent(reviewId, checked);
    }
}
