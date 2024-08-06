package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackerentity.entity.Course;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerprofessor.dto.student.StudentRequestFilterForStatistic;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class StudentSpecificationForStatistic implements Specification {
    private StudentRequestFilterForStatistic studentRequestFilterForStatistic;
    private List<Course> courses;

    public StudentSpecificationForStatistic(StudentRequestFilterForStatistic studentRequestFilterForStatistic, List<Course> courses) {
        this.studentRequestFilterForStatistic = studentRequestFilterForStatistic;
        this.courses = courses;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(nonNull(studentRequestFilterForStatistic.getSearch()) && !studentRequestFilterForStatistic.getSearch().isBlank()) {
            studentRequestFilterForStatistic.setSearch("%" + studentRequestFilterForStatistic.getSearch() + "%");
            predicates.add(
                criteriaBuilder.or(
                    criteriaBuilder.like(
                        criteriaBuilder.concat(
                            criteriaBuilder.concat(
                                criteriaBuilder.concat(
                                    criteriaBuilder.lower(root.get("lastName")),
                                    " "
                                ),
                                criteriaBuilder.lower(root.get("name"))
                            ),
                                criteriaBuilder.concat(" ",
                                criteriaBuilder.lower(root.get("middleName"))
                            )
                        ), studentRequestFilterForStatistic.getSearch()
                    ),
                    criteriaBuilder.like(root.get("telegram"), studentRequestFilterForStatistic.getSearch()),
                    criteriaBuilder.like(root.get("groupName"), studentRequestFilterForStatistic.getSearch())
                )
            );
        }
        Join<Student, Course> courseJoin = root.join("courses", JoinType.INNER);
        predicates.add(criteriaBuilder.equal(courseJoin.get("id"), studentRequestFilterForStatistic.getCourseId()));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
