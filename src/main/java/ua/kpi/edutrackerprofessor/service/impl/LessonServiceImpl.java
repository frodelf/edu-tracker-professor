package ua.kpi.edutrackerprofessor.service.impl;

import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.repository.LessonRepository;
import ua.kpi.edutrackerprofessor.service.LessonService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    @Override
    public long countByCourseId(long courseId) {
        return lessonRepository.countByCourseId(courseId);
    }
}