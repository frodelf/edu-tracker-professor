package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper = new ReviewMapper();

    @Override
    public long countAllVisitedLessonByStudentIdAndCourseId(Long studentId, Long courseId) {
        log.info("ReviewServiceImpl countAllVisitedLessonByStudentIdAndCourseId start");
        long result = reviewRepository.countByStudentIdAndCourseId(studentId, courseId);
        log.info("ReviewServiceImpl countAllVisitedLessonByStudentIdAndCourseId finish");
        return result;
    }

    @Override
    public long countAllVisitedLessonByStudentId(Long studentId) {
        log.info("ReviewServiceImpl countAllVisitedLessonByStudentId start");
        long result = reviewRepository.countByStudentId(studentId);
        log.info("ReviewServiceImpl countAllVisitedLessonByStudentId finish");
        return result;
    }

    @Override
    @Transactional
    public Review save(Review review) {
        log.info("ReviewServiceImpl save start");
        Review savedReview = reviewRepository.save(review);
        log.info("ReviewServiceImpl save finish");
        return savedReview;
    }

    @Override
    public Long countByLessonAndPresentTrue(Lesson lesson) {
        log.info("ReviewServiceImpl countByLessonAndPresentTrue start");
        long result = reviewRepository.countByLessonAndPresentTrue(lesson);
        log.info("ReviewServiceImpl countByLessonAndPresentTrue finish");
        return result;
    }

    @Override
    public Page<ReviewDtoForEdit> getAllForLessonEdit(ReviewRequestForFilter reviewRequestForFilter) {
        log.info("ReviewServiceImpl getAllForLessonEdit start");
        Pageable pageable = PageRequest.of(reviewRequestForFilter.getPage(), reviewRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        ReviewSpecification reviewSpecification = new ReviewSpecification(reviewRequestForFilter);
        Page<ReviewDtoForEdit> result = reviewMapper.toDtoListForEdit(reviewRepository.findAll(reviewSpecification, pageable));
        log.info("ReviewServiceImpl getAllForLessonEdit finish");
        return result;
    }

    @Override
    @Transactional
    public void updatePresent(Long reviewId, Boolean checked) {
        log.info("ReviewServiceImpl updatePresent start");
        Review review = getById(reviewId);
        review.setPresent(checked);
        save(review);
        log.info("ReviewServiceImpl updatePresent finish");
    }

    @Override
    public Review getById(Long id) {
        log.info("ReviewServiceImpl getById start");
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Review with id = "+id+" not found")
        );
        log.info("ReviewServiceImpl getById finish");
        return review;
    }
}
