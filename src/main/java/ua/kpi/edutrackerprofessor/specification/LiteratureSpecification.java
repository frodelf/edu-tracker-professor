package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static ua.kpi.edutrackerprofessor.validation.ValidUtil.notNullAndBlank;

public class LiteratureSpecification implements Specification<Literature> {
    private LiteratureRequestForFilter requestForFilter;
    private List<Course> courses;

    public LiteratureSpecification(LiteratureRequestForFilter requestForFilter, List<Course> courses) {
        this.requestForFilter = requestForFilter;
        this.courses = courses;
    }

    @Override
    public Predicate toPredicate(Root<Literature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(notNullAndBlank(requestForFilter.getName())){
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + requestForFilter.getName() + "%"));
        }
        if(nonNull(requestForFilter.getCourse())){
            predicates.add(criteriaBuilder.equal(root.get("course").get("id"), requestForFilter.getCourse()));
        }
        else {
            predicates.add(root.get("course").get("id").in(courses.stream()
                    .map(Course::getId)
                    .collect(Collectors.toList())));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
