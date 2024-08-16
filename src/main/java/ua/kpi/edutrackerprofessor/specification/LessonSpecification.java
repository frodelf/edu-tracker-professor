package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Lesson;
import ua.kpi.edutrackerprofessor.dto.lesson.LessonRequestForFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LessonSpecification implements Specification<Lesson> {
    private final LessonRequestForFilter lessonRequestForFilter;
    private final List<Course> courses;

    public LessonSpecification(LessonRequestForFilter lessonRequestForFilter, List<Course> courses) {
        this.lessonRequestForFilter = lessonRequestForFilter;
        this.courses = courses;
    }

    @Override
    public Predicate toPredicate(Root<Lesson> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(nonNull(lessonRequestForFilter.getDate())){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), lessonRequestForFilter.getDate()));
        }
        if(nonNull(lessonRequestForFilter.getCourseId())){
            predicates.add(criteriaBuilder.equal(root.get("course").get("id"), lessonRequestForFilter.getCourseId()));
        }else {
            predicates.add(root.get("course").get("id").in(courses.stream()
                    .map(Course::getId)
                    .collect(Collectors.toList())));
        }
        if(nonNull(lessonRequestForFilter.getStatusLesson())){
            predicates.add(criteriaBuilder.equal(root.get("status"), lessonRequestForFilter.getStatusLesson()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
