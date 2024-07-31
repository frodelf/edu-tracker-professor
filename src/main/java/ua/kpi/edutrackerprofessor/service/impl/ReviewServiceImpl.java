package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Review;
import ua.kpi.edutrackerprofessor.dto.review.ReviewDtoForEdit;
import ua.kpi.edutrackerprofessor.dto.review.ReviewRequestForFilter;
import ua.kpi.edutrackerprofessor.mapper.ReviewMapper;
import ua.kpi.edutrackerprofessor.repository.ReviewRepository;
import ua.kpi.edutrackerprofessor.service.ReviewService;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerprofessor.specification.ReviewSpecification;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper = new ReviewMapper();
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
    @Override
    public Page<ReviewDtoForEdit> getAllForLessonEdit(ReviewRequestForFilter reviewRequestForFilter) {
        Pageable pageable = PageRequest.of(reviewRequestForFilter.getPage(), reviewRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        ReviewSpecification reviewSpecification = new ReviewSpecification(reviewRequestForFilter);
        return reviewMapper.toDtoListForEdit(reviewRepository.findAll(reviewSpecification, pageable));
    }
}