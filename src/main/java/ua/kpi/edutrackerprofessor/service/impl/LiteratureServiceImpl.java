package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForViewAll;
import ua.kpi.edutrackerprofessor.mapper.LiteratureMapper;
import ua.kpi.edutrackerprofessor.repository.LiteratureRepository;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.LiteratureService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerprofessor.specification.LiteratureSpecification;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LiteratureServiceImpl implements LiteratureService {
    private final LiteratureRepository literatureRepository;
    private final LiteratureMapper literatureMapper = new LiteratureMapper();
    private final ProfessorService professorService;
    private final CourseService courseService;
    @Override
    public Page<LiteratureResponseForViewAll> getAll(LiteratureRequestForFilter literatureRequestForFilter) {
        Pageable pageable = PageRequest.of(literatureRequestForFilter.getPage(), literatureRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        LiteratureSpecification literatureSpecification = new LiteratureSpecification(literatureRequestForFilter, professorService.getAuthProfessor().getCourses());
        return literatureMapper.toDtoListForViewAll(literatureRepository.findAll(literatureSpecification, pageable));
    }
    @Override
    @Transactional
    public long add(LiteratureRequestForAdd literatureRequestForAdd) {
        return save(literatureMapper.toDtoForAdd(literatureRequestForAdd, this, courseService)).getId();
    }
    @Override
    @Transactional
    public Literature save(Literature literature) {
        return literatureRepository.save(literature);
    }
    @Override
    public Literature getById(long id) {
        isLiteratureAssignedToProfessor(id);
        return literatureRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Literature with id = "+id+" not found")
        );
    }
    @Override
    public void isLiteratureAssignedToProfessor(long literatureId) {
        Literature literature = literatureRepository.findById(literatureId).orElseThrow(
                () -> new EntityNotFoundException("Literature with id = "+literatureId+" not found")
        );
        if (literature.getCourse()==null  || !Objects.equals(literature.getCourse().getProfessor().getId(), professorService.getAuthProfessor().getId())) throw new AccessDeniedException("You don't have access to this page");
    }
}