package ua.kpi.edutrackerprofessor.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.LiteratureService;

import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LiteratureMapper {
    public Page<LiteratureResponseForViewAll> toDtoListForViewAll(Page<Literature> literatures) {
        return new PageImpl<>(literatures.getContent().stream()
                .map(this::toDtoForViewAll)
                .collect(Collectors.toList()), literatures.getPageable(), literatures.getTotalElements());
    }

    private LiteratureResponseForViewAll toDtoForViewAll(Literature literature) {
        LiteratureResponseForViewAll literatureResponseForViewAll = new LiteratureResponseForViewAll();
        literatureResponseForViewAll.setId(literature.getId());
        literatureResponseForViewAll.setName(literature.getName());
        literatureResponseForViewAll.setLink(literature.getLink());
        if(nonNull(literature.getCourse()))literatureResponseForViewAll.setCourse(Collections.singletonMap(String.valueOf(literature.getCourse().getId()), literature.getCourse().getName()));
        return literatureResponseForViewAll;
    }
    public Literature toDtoForAdd(LiteratureRequestForAdd literatureRequestForAdd, LiteratureService literatureService, CourseService courseService) {
        Literature literature = new Literature();
        if(literatureRequestForAdd.getId()!=null)literature = literatureService.getById(literatureRequestForAdd.getId());
        if(literatureRequestForAdd.getName()!=null)literature.setName(literatureRequestForAdd.getName());
        if(literatureRequestForAdd.getLink()!=null)literature.setLink(literatureRequestForAdd.getLink());
        if(literatureRequestForAdd.getCourse()!=null)literature.setCourse(courseService.getById(literatureRequestForAdd.getCourse()));
        return literature;
    }
}
