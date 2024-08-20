package ua.kpi.edutrackerprofessor.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.kpi.edutrackerentity.entity.Review;
import ua.kpi.edutrackerprofessor.dto.review.ReviewRequestForFilter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static ua.kpi.edutrackerprofessor.validation.ValidUtil.notNullAndBlank;

public class ReviewSpecification implements Specification<Review> {
    private ReviewRequestForFilter reviewRequestForFilter;

    public ReviewSpecification(ReviewRequestForFilter reviewRequestForFilter) {
        this.reviewRequestForFilter = reviewRequestForFilter;
    }

    @Override
    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(notNullAndBlank(reviewRequestForFilter.getFullName())){
            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.concat(
                                    criteriaBuilder.concat(
                                            criteriaBuilder.concat(
                                                    criteriaBuilder.lower(root.get("student").get("groupName")),
                                                    " "
                                            ),
                                            criteriaBuilder.lower(root.get("student").get("lastName"))
                                    ),
                                    criteriaBuilder.concat(
                                            " ",
                                            criteriaBuilder.lower(root.get("student").get("name"))
                                    )
                            ),
                            "%" + reviewRequestForFilter.getFullName().toLowerCase() + "%"
                    )
            );
        }
        predicates.add(criteriaBuilder.equal(root.get("lesson").get("id"), reviewRequestForFilter.getLessonId()));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}