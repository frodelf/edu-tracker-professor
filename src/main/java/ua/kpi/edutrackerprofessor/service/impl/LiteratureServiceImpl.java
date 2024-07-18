package ua.kpi.edutrackerprofessor.service.impl;

import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.repository.LiteratureRepository;
import ua.kpi.edutrackerprofessor.service.LiteratureService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiteratureServiceImpl implements LiteratureService {
    private final LiteratureRepository literatureRepository;
    private final ProfessorService professorService;
    @Override
    public Page<Literature> getAll(LiteratureRequestForFilter literatureRequestForFilter) {
        Pageable pageable = PageRequest.of(literatureRequestForFilter.getPage(), literatureRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        return literatureRepository.findDistinctByCourseIn(professorService.getAuthProfessor().getCourses(), pageable);
    }
}