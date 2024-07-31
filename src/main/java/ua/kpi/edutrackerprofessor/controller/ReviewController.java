package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kpi.edutrackerprofessor.dto.review.ReviewDtoForEdit;
import ua.kpi.edutrackerprofessor.dto.review.ReviewRequestForFilter;
import ua.kpi.edutrackerprofessor.service.ReviewService;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/get-all-for-lesson-edit")
    public ResponseEntity<Page<ReviewDtoForEdit>> getAllForLessonEdit(@ModelAttribute @Valid ReviewRequestForFilter reviewRequestForFilter){
        return ResponseEntity.ok(reviewService.getAllForLessonEdit(reviewRequestForFilter));
    }
}