package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import org.springframework.data.domain.Page;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForViewAll;

public interface LiteratureService {
    Page<LiteratureResponseForViewAll> getAll(LiteratureRequestForFilter literatureRequestForFilter);
    long add(LiteratureRequestForAdd literatureRequestForAdd);
    Literature save(Literature literature);
    Literature getById(long id);
    void isLiteratureAssignedToProfessor(long literatureId);
    void deleteById(Long id);
    LiteratureResponseForAdd getByIdForAdd(Long id);
}