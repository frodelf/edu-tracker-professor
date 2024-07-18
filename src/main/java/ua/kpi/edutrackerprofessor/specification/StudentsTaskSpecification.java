package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerentity.entity.StudentsTask;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentsTaskSpecification implements Specification<StudentsTask> {
    private final StudentTaskRequestForFilter studentTaskRequestForFilter;

    public StudentsTaskSpecification(StudentTaskRequestForFilter studentTaskRequestForFilter) {
        this.studentTaskRequestForFilter = studentTaskRequestForFilter;
    }

    @Override
    public Predicate toPredicate(@NotNull Root<StudentsTask> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (!studentTaskRequestForFilter.getGroupName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("student").get("groupName"), "%" + studentTaskRequestForFilter.getGroupName() + "%"));
        }
        if (studentTaskRequestForFilter.getTaskId()!=null) {
            predicates.add(criteriaBuilder.equal(root.get("task").get("id"), studentTaskRequestForFilter.getTaskId()));
        }
        if (!studentTaskRequestForFilter.getFullName().isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.concat(
                                    criteriaBuilder.concat(
                                            criteriaBuilder.lower(root.get("student").get("lastName")),
                                            " "
                                    ),
                                    criteriaBuilder.lower(root.get("student").get("name"))
                            ),
                            "%" + studentTaskRequestForFilter.getFullName().toLowerCase() + "%"
                    )
            );
        }
        if (!studentTaskRequestForFilter.getTelegram().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("student").get("telegram"), "%" + studentTaskRequestForFilter.getTelegram() + "%"));
        }
        if (studentTaskRequestForFilter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), studentTaskRequestForFilter.getStatus()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
