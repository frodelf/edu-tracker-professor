package ua.kpi.edutrackerprofessor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureRequestForFilter;
import ua.kpi.edutrackerentity.entity.Literature;
import ua.kpi.edutrackerprofessor.dto.literature.LiteratureResponseForAdd;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class LiteratureServiceImpl implements LiteratureService {
    private final LiteratureRepository literatureRepository;
    private final LiteratureMapper literatureMapper = new LiteratureMapper();
    private final ProfessorService professorService;
    private final CourseService courseService;

    @Override
    public Page<LiteratureResponseForViewAll> getAll(LiteratureRequestForFilter literatureRequestForFilter) {
        log.info("getAll start");
        Pageable pageable = PageRequest.of(literatureRequestForFilter.getPage(), literatureRequestForFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        LiteratureSpecification literatureSpecification = new LiteratureSpecification(literatureRequestForFilter, professorService.getAuthProfessor().getCourses());
        Page<LiteratureResponseForViewAll> result = literatureMapper.toDtoListForViewAll(literatureRepository.findAll(literatureSpecification, pageable));
        log.info("getAll finish");
        return result;
    }

    @Override
    @Transactional
    public long add(LiteratureRequestForAdd literatureRequestForAdd) {
        log.info("add start");
        long id = save(literatureMapper.toEntityForAdd(literatureRequestForAdd, this, courseService)).getId();
        log.info("add finish");
        return id;
    }

    @Override
    @Transactional
    public Literature save(Literature literature) {
        log.info("save start");
        Literature savedLiterature = literatureRepository.save(literature);
        log.info("save finish");
        return savedLiterature;
    }

    @Override
    public Literature getById(long id) {
        log.info("getById start");
        isLiteratureAssignedToProfessor(id);
        Literature literature = literatureRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Literature with id = "+id+" not found")
        );
        log.info("getById finish");
        return literature;
    }

    @Override
    public void isLiteratureAssignedToProfessor(long literatureId) {
        log.info("isLiteratureAssignedToProfessor start");
        Literature literature = literatureRepository.findById(literatureId).orElseThrow(
                () -> new EntityNotFoundException("Literature with id = "+literatureId+" not found")
        );
        if (literature.getCourse() == null || !literature.getCourse().getProfessor().getId().equals(professorService.getAuthProfessor().getId())) {
            throw new AccessDeniedException("You don't have access to this page");
        }
        log.info("isLiteratureAssignedToProfessor finish");
    }

    @Override
    public void deleteById(Long id) {
        log.info("deleteById start");
        literatureRepository.deleteById(id);
        log.info("deleteById finish");
    }

    @Override
    public LiteratureResponseForAdd getByIdForAdd(Long id) {
        log.info("getByIdForAdd start");
        LiteratureResponseForAdd response = literatureMapper.toDtoForAdd(getById(id));
        log.info("getByIdForAdd finish");
        return response;
    }
}
