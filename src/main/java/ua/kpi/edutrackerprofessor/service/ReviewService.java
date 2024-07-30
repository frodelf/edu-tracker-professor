package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Review;

public interface ReviewService {
    long countAllVisitedLessonByStudentIdAndCourseId(Long studentId, Long courseId);
    long countAllVisitedLessonByStudentId(Long studentId);
    Review save(Review review);
    Long countByLessonAndPresentTrue(Lesson lesson);
}