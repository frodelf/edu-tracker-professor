package ua.kpi.edutrackerprofessor.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ua.kpi.edutrackerentity.entity.Review;
import ua.kpi.edutrackerprofessor.dto.review.ReviewDtoForEdit;

import java.util.stream.Collectors;

public class ReviewMapper {
    public Page<ReviewDtoForEdit> toDtoListForEdit(Page<Review> reviews) {
        return new PageImpl<>(
                reviews.getContent().stream()
                        .map(this::toDtoForEdit)
                        .collect(Collectors.toList()),
                reviews.getPageable(),
                reviews.getTotalElements()
        );
    }
    public ReviewDtoForEdit toDtoForEdit(Review review) {
        ReviewDtoForEdit dto = new ReviewDtoForEdit();
        dto.setId(review.getId());
        dto.setPresent(review.getPresent());
        dto.setStudentId(review.getStudent().getId());
        dto.setFullName(review.getStudent().getGroupName() + " " + review.getStudent().getLastName() + " " + review.getStudent().getName());
        return dto;
    }
}