package ua.kpi.edutrackerprofessor.service.impl;

import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.task.*;
import ua.kpi.edutrackerentity.entity.Task;
import ua.kpi.edutrackerprofessor.mapper.TaskMapper;
import ua.kpi.edutrackerprofessor.repository.TaskRepository;
import ua.kpi.edutrackerprofessor.service.CourseService;
import ua.kpi.edutrackerprofessor.service.MinioService;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.TaskService;
import ua.kpi.edutrackerprofessor.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.util.Objects.nonNull;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final MinioService minioService;
    private final TaskMapper taskMapper = new TaskMapper();
    //TODO доробити методи
    @Override
    public long countAllTasksByCourseId(long courseId) {
        return 0;
    }
    @Override
    public long countAllOpenTasksByCourseId(long courseId) {
        return 0;
    }
    @Override
    public long countAllCloseTasksByCourseId(long courseId) {
        return 0;
    }
    @Override
    public Page<TaskResponseViewAll> getAll(TaskRequestFilter taskRequestFilter) {
        Pageable pageable = PageRequest.of(taskRequestFilter.getPage(), taskRequestFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        if(nonNull(taskRequestFilter.getCourseId()) || !taskRequestFilter.getName().isBlank() || nonNull(taskRequestFilter.getStatus()) || nonNull(taskRequestFilter.getDeadline())) {
            TaskSpecification taskSpecification = new TaskSpecification(taskRequestFilter);
            return taskMapper.toDtoListForViewAll(taskRepository.findAll(taskSpecification, pageable));
        }
        return taskMapper.toDtoListForViewAll(taskRepository.findDistinctByCourseIn(professorService.getAuthProfessor().getCourses(), pageable));
    }
    @Override
    public void deleteById(Long taskId) {
        taskRepository.delete(getById(taskId));
    }
    @Override
    public Task getById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task with id = "+taskId+" not found")
        );
        courseService.isCourseAssignedToProfessor(task.getCourse().getId());
        return task;
    }
    @Override
    public TaskResponseForAdd getByIdForAdd(Long taskId) {
        return taskMapper.toDtoForAdd(getById(taskId));
    }
    @Override
    @Transactional
    public void add(TaskRequestForAdd taskRequestForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Task task = taskMapper.toEntityForAdd(taskRequestForAdd, this, courseService);
        if(taskRequestForAdd.getFile() != null)task.setFile(minioService.putMultipartFile(taskRequestForAdd.getFile()));
        save(task);
    }
    @Override
    @Transactional
    public void save(Task task) {
        taskRepository.save(task);
    }
    @Override
    public TaskResponseForView getByIdForView(Long id) {
        return taskMapper.toDtoForViewPage(getById(id));
    }
}
