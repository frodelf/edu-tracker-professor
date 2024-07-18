package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerentity.entity.Literature;
import org.springframework.data.domain.Page;

public interface LiteratureService {
    Page<Literature> getAll(LiteratureRequestForFilter literatureRequestForFilter);
}