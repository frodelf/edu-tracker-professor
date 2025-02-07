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
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerentity.entity.enums.StatusLesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForStart;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonResponseForViewAll;
import ua.kpi.edutrackerprofessor.mapper.LessonMapper;
import ua.kpi.edutrackerprofessor.repository.CourseRepository;
import ua.kpi.edutrackerprofessor.repository.LessonRepository;
import ua.kpi.edutrackerprofessor.service.*;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerprofessor.specification.LessonSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ReviewService reviewService;
    /*
    * I use the repository itself, because if I use the service,
    * an error is raised that indicates cyclic dependencies
    */
    private final CourseRepository courseRepository;

    private final StudentService studentService;
    private final ProfessorService professorService;
    private final LessonMapper lessonMapper = new LessonMapper();

    @Override
    public long countByCourseId(long courseId) {
        log.info("countByCourseId start");
        long count = lessonRepository.countByCourseId(courseId);
        log.info("countByCourseId finish");
        return count;
    }

    @Override
    public Map<String, String> getStatusForSelect() {
        log.info("getStatusForSelect start");
        Map<String, String> result = new HashMap<>();
        for (StatusLesson value : StatusLesson.values()) {
            result.put(value.name(), value.toString());
        }
        log.info("getStatusForSelect finish");
        return result;
    }

    @Override
    @Transactional
    public Long start(LessonRequestForStart lessonRequestForStart) {
        log.info("start start");
        Lesson lesson = lessonMapper.toEntityForAdd(lessonRequestForStart, courseRepository);
        lesson = save(lesson);
        if(nonNull(lessonRequestForStart.getGroups())){
            for (String group : lessonRequestForStart.getGroups()) {
                for (Student student : studentService.getAllByGroup(group)) {
                    Review review = new Review();
                    review.setLesson(lesson);
                    review.setStudent(student);
                    review.setPresent(false);
                    reviewService.save(review);
                }
            }
        }else {
            for (Student student : studentService.getAllByCourseId(lessonRequestForStart.getCourseId())) {
                Review review = new Review();
                review.setLesson(lesson);
                review.setStudent(student);
                review.setPresent(false);
                reviewService.save(review);
            }
        }
        log.info("start finish");
        return lesson.getId();
    }

    @Override
    @Transactional
    public Lesson save(Lesson lesson) {
        log.info("save start");
        Lesson savedLesson = lessonRepository.save(lesson);
        log.info("save finish");
        return savedLesson;
    }

    @Override
    public Lesson getById(Long id) {
        log.info("getById start");
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Lesson with id = " + id + " not found")
        );
        log.info("getById finish");
        return lesson;
    }

    @Override
    public Page<LessonResponseForViewAll> getAll(LessonRequestForFilter lessonRequestForFilter) {
        log.info("getAll start");
        Pageable pageable = PageRequest.of(lessonRequestForFilter.getPage(), lessonRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        LessonSpecification lessonSpecification = new LessonSpecification(lessonRequestForFilter, professorService.getAuthProfessor().getCourses());
        Page<LessonResponseForViewAll> result = lessonMapper.toDtoListForViewAll(lessonRepository.findAll(lessonSpecification, pageable), reviewService);
        log.info("getAll finish");
        return result;
    }

    @Override
    @Transactional
    public void finish(Long lessonId) {
        log.info("finish start");
        Lesson lesson = getById(lessonId);
        lesson.setStatus(StatusLesson.FINISHED);
        save(lesson);
        log.info("finish finish");
    }

    @Override
    public Long countAllByStatus(StatusLesson statusLesson) {
        log.info("countAllByStatus start");
        long count = lessonRepository.countAllByCourseInAndStatus(professorService.getAuthProfessor().getCourses(), statusLesson);
        log.info("countAllByStatus finish");
        return count;
    }

    @Override
    public Map<String, String> getDateCountMap(Long courseId) {
        log.info("getDateCountMap start");
        List<Object[]> results = lessonRepository.countStudentsByDateAndCourseId(courseId);
        Map<String, String> resultMap = new HashMap<>();
        for (Object[] result : results) {
            String date = result[0].toString();
            String studentCount = result[1].toString();
            resultMap.put(date, studentCount);
        }
        log.info("getDateCountMap finish");
        return resultMap;
    }
}