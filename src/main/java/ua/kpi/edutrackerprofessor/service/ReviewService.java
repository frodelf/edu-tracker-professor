package ua.kpi.edutrackerprofessor.service;

import org.springframework.data.domain.Page;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Review;
import ua.kpi.edutrackerprofessor.dto.review.ReviewDtoForEdit;
import ua.kpi.edutrackerprofessor.dto.review.ReviewRequestForFilter;

public interface ReviewService {
    long countAllVisitedLessonByStudentIdAndCourseId(Long studentId, Long courseId);
    long countAllVisitedLessonByStudentId(Long studentId);
    Review save(Review review);
    Long countByLessonAndPresentTrue(Lesson lesson);
    Page<ReviewDtoForEdit> getAllForLessonEdit(ReviewRequestForFilter reviewRequestForFilter);
    void updatePresent(Long reviewId, Boolean checked);
    Review getById(Long id);
}