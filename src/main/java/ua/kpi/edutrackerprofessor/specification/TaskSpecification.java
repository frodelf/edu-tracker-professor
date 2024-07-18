package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.kpi.edutrackerprofessor.dto.task.TaskRequestFilter;
import ua.kpi.edutrackerentity.entity.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification implements Specification<Task> {
    private final TaskRequestFilter taskRequestFilter;
    public TaskSpecification(TaskRequestFilter taskRequestFilter) {
        this.taskRequestFilter = taskRequestFilter;
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
        if(taskRequestFilter.getStatus()!=null){
            predicates.add(criteriaBuilder.equal(root.get("status"), taskRequestFilter.getStatus()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
