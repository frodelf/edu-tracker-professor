package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerentity.entity.Review;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerentity.entity.enums.StatusLesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;
import ua.kpi.edutrackerprofessor.mapper.LessonMapper;
import ua.kpi.edutrackerprofessor.repository.LessonRepository;
import ua.kpi.edutrackerprofessor.service.*;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerprofessor.specification.LessonSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ReviewService reviewService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final LessonMapper lessonMapper = new LessonMapper();
    @Override
    public long countByCourseId(long courseId) {
        return lessonRepository.countByCourseId(courseId);
    }
    @Override
    public Map<String, String> getStatusForSelect() {
        Map<String, String> result = new HashMap<>();
        for (StatusLesson value : StatusLesson.values()) {
            result.put(value.name(), value.toString());
        }
        return result;
    }
    @Override
    @Transactional
    public Long start(LessonRequestForStart lessonRequestForStart) {
        Lesson lesson = lessonMapper.toEntityForAdd(lessonRequestForStart, courseService);
        lesson = save(lesson);
        for (String group : lessonRequestForStart.getGroups()) {
            for (Student student : studentService.getAllByGroup(group)) {
                Review review = new Review();
                review.setLesson(lesson);
                review.setStudent(student);
                review.setPresent(false);
                reviewService.save(review);
            }
        }
        return lesson.getId();
    }
    @Override
    @Transactional
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Lesson with id = "+id+" not found")
        );
    }
    @Override
    public Page<LessonResponseForViewAll> getAll(LessonRequestForFilter lessonRequestForFilter) {
        Pageable pageable = PageRequest.of(lessonRequestForFilter.getPage(), lessonRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        LessonSpecification lessonSpecification = new LessonSpecification(lessonRequestForFilter, professorService.getAuthProfessor().getCourses());
        return lessonMapper.toDtoListForViewAll(lessonRepository.findAll(lessonSpecification, pageable), reviewService);
    }
    @Override
    @Transactional
    public void finish(Long lessonId) {
        Lesson lesson = getById(lessonId);
        lesson.setStatus(StatusLesson.FINISHED);
        save(lesson);
    }
}