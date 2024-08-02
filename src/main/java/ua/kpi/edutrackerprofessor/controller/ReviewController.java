package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    @PutMapping("/update-present")
    public ResponseEntity<String> updatePresent(@RequestParam Long reviewId, @RequestParam Boolean checked){
        reviewService.updatePresent(reviewId, checked);
        return ResponseEntity.ok("updated");
    }
}