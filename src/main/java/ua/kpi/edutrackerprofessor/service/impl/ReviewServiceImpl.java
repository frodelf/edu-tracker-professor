package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Review;
import ua.kpi.edutrackerprofessor.repository.ReviewRepository;
import ua.kpi.edutrackerprofessor.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Override
    public long countAllVisitedLessonByStudentIdAndCourseId(Long studentId, Long courseId) {
        return reviewRepository.countByStudentIdAndCourseId(studentId, courseId);
    }
    @Override
    public long countAllVisitedLessonByStudentId(Long studentId) {
        return reviewRepository.countByStudentId(studentId);
    }
    @Override
    @Transactional
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
    @Override
    public Long countByLessonAndPresentTrue(Lesson lesson) {
        return reviewRepository.countByLessonAndPresentTrue(lesson);
    }
}