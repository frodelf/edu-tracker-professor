package ua.kpi.edutrackerprofessor.service;

import org.springframework.data.domain.Page;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;

import java.util.Map;

public interface LessonService {
    long countByCourseId(long courseId);
    Map<String, String> getStatusForSelect();
    Long start(LessonRequestForStart lessonRequestForStart);
    Lesson save(Lesson lesson);
    Lesson getById(Long id);
    Page<LessonResponseForViewAll> getAll(LessonRequestForFilter lessonRequestForFilter);
    void finish(Long lessonId);
    Long countAll();
    Map<String, String> getDateCountMap(Long courseId);
}