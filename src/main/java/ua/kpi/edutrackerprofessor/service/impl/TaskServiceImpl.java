package ua.kpi.edutrackerprofessor.service.impl;

import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;
import ua.kpi.edutrackerprofessor.dto.task.*;
import ua.kpi.edutrackerentity.entity.Task;
import ua.kpi.edutrackerprofessor.mapper.TaskMapper;
import ua.kpi.edutrackerprofessor.repository.TaskRepository;
import ua.kpi.edutrackerprofessor.service.*;
import ua.kpi.edutrackerprofessor.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static ua.kpi.edutrackerprofessor.validation.ValidUtil.notNullAndBlank;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CourseService courseService;
    private final MinioService minioService;
    private final ProfessorService professorService;
    private final StudentsTaskService studentsTaskService;
    private final TaskMapper taskMapper = new TaskMapper();
    @Override
    public long countAllTasksByCourseId(long courseId) {
        return taskRepository.countAllByCourseId(courseId);
    }
    @Override
    public long countAllOpenTasksByCourseId(long courseId) {
        return taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.OPEN);
    }
    @Override
    public long countAllCloseTasksByCourseId(long courseId) {
        return taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.CLOSE);
    }
    @Override
    public Page<TaskResponseViewAll> getAll(TaskRequestFilter taskRequestFilter) {
        Pageable pageable = PageRequest.of(taskRequestFilter.getPage(), taskRequestFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        if(nonNull(taskRequestFilter.getCourseId()) || notNullAndBlank(taskRequestFilter.getName()) || nonNull(taskRequestFilter.getStatus()) || nonNull(taskRequestFilter.getDeadline())) {
            TaskSpecification taskSpecification = new TaskSpecification(taskRequestFilter, professorService.getAuthProfessor().getCourses());
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
    @Override
    public Map<String, String> getAllCloseForSelectByCourseId(Long courseId) {
        Map<String, String> forSelect = new HashMap<>();
        for (Task task : taskRepository.findAllByCourseIdAndStatus(courseId, StatusTask.CLOSE)) {
            forSelect.put(task.getId().toString(), task.getName());
        }
        return forSelect;
    }
    @Override
    @Transactional
    public void open(TaskRequestForOpen taskRequestForOpen) {
        Task task = taskMapper.toEntityForOpen(taskRequestForOpen, this);
        save(task);
        studentsTaskService.triggerToOpenTask(task);
    }
    @Override
    @Transactional
    public void close(List<Long> taskId) {
        for (Long id : taskId) {
            Task task = getById(id);
            task.setStatus(StatusTask.CLOSE);
            task.setDeadline(null);
            save(task);
        }
    }
    @Override
    public Map<String, String> getAllOpenForSelectByCourseId(Long courseId) {
        Map<String, String> forSelect = new HashMap<>();
        for (Task task : taskRepository.findAllByCourseIdAndStatus(courseId, StatusTask.OPEN)) {
            forSelect.put(task.getId().toString(), task.getName());
        }
        return forSelect;
    }

    @Override
    public Long countAllTask() {
        return taskRepository.countAllByCourseIn(professorService.getAuthProfessor().getCourses());
    }
}
