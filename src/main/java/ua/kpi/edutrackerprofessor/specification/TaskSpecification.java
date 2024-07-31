package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;
import ua.kpi.edutrackerprofessor.dto.task.TaskRequestFilter;
import ua.kpi.edutrackerentity.entity.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskSpecification implements Specification<Task> {
    private final TaskRequestFilter taskRequestFilter;
    private List<Course> courses;

    public TaskSpecification(TaskRequestFilter taskRequestFilter, List<Course> courses) {
        this.taskRequestFilter = taskRequestFilter;
        this.courses = courses;
    }
    @Override
    public Predicate toPredicate(@NotNull Root<Task> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(taskRequestFilter.getName()!=null){
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + taskRequestFilter.getName() + "%"));
        }
        if(taskRequestFilter.getCourseId()!=null){
            predicates.add(criteriaBuilder.equal(root.get("course").get("id"), taskRequestFilter.getCourseId()));
        }
        if(taskRequestFilter.getDeadline()!=null){
            predicates.add(criteriaBuilder.equal(root.get("status"), StatusTask.OPEN));
            Predicate condition1 = criteriaBuilder.lessThanOrEqualTo(root.get("deadline"), taskRequestFilter.getDeadline());
            Predicate condition2 = criteriaBuilder.isNull(root.get("deadline"));
            predicates.add(criteriaBuilder.or(condition1, condition2));
        }
        if(taskRequestFilter.getStatus()!=null){
            predicates.add(criteriaBuilder.equal(root.get("status"), taskRequestFilter.getStatus()));
        }
        predicates.add(root.get("course").get("id").in(courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList())));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
