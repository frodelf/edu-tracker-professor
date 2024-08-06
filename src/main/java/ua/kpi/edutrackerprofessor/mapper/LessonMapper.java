package ua.kpi.edutrackerprofessor.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.enums.StatusLesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;
import ua.kpi.edutrackerprofessor.repository.CourseRepository;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.ReviewService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LessonMapper {
    public Lesson toEntityForAdd(LessonRequestForStart lessonRequestForStart, CourseRepository courseRepository) {
        Lesson lesson = new Lesson();
        lesson.setStatus(StatusLesson.IN_PROGRESS);
        lesson.setDate(LocalDateTime.now());
        lesson.setCourse(courseRepository.findById(lessonRequestForStart.getCourseId()).orElseThrow(
                () -> new EntityNotFoundException("Course with id = "+lessonRequestForStart.getCourseId()+" not found")
        ));
        return lesson;
    }
    private LessonResponseForViewAll toDtoForViewAll(Lesson lesson, ReviewService reviewService) {
        LessonResponseForViewAll lessonResponseForViewAll = new LessonResponseForViewAll();
        lessonResponseForViewAll.setId(lesson.getId());
        if(nonNull(lesson.getCourse()))lessonResponseForViewAll.setCourse(Collections.singletonMap(lesson.getCourse().getId().toString(), lesson.getCourse().getName()));
        lessonResponseForViewAll.setStatus(lesson.getStatus());
        lessonResponseForViewAll.setDate(lesson.getDate());
        lessonResponseForViewAll.setPresentStudent(reviewService.countByLessonAndPresentTrue(lesson));
        return lessonResponseForViewAll;
    }
    public Page<LessonResponseForViewAll> toDtoListForViewAll(Page<Lesson> lessons, ReviewService reviewService) {
        return new PageImpl<>(lessons.getContent().stream()
                .map(entity -> this.toDtoForViewAll(entity, reviewService))
                .collect(Collectors.toList()), lessons.getPageable(), lessons.getTotalElements());
    }
}