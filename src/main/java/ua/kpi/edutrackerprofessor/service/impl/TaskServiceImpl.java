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

import lombok.extern.log4j.Log4j2;

@Log4j2
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
        log.info("TaskServiceImpl countAllTasksByCourseId start");
        long result = taskRepository.countAllByCourseId(courseId);
        log.info("TaskServiceImpl countAllTasksByCourseId finish");
        return result;
    }

    @Override
    public long countAllOpenTasksByCourseId(long courseId) {
        log.info("TaskServiceImpl countAllOpenTasksByCourseId start");
        long result = taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.OPEN);
        log.info("TaskServiceImpl countAllOpenTasksByCourseId finish");
        return result;
    }

    @Override
    public long countAllCloseTasksByCourseId(long courseId) {
        log.info("TaskServiceImpl countAllCloseTasksByCourseId start");
        long result = taskRepository.countAllByCourseIdAndStatus(courseId, StatusTask.CLOSE);
        log.info("TaskServiceImpl countAllCloseTasksByCourseId finish");
        return result;
    }

    @Override
    public Page<TaskResponseViewAll> getAll(TaskRequestFilter taskRequestFilter) {
        log.info("TaskServiceImpl getAll start");
        Pageable pageable = PageRequest.of(taskRequestFilter.getPage(), taskRequestFilter.getPageSize(), Sort.by(Sort.Order.desc("id")));
        Page<TaskResponseViewAll> result;
        if(nonNull(taskRequestFilter.getCourseId()) || notNullAndBlank(taskRequestFilter.getName()) || nonNull(taskRequestFilter.getStatus()) || nonNull(taskRequestFilter.getDeadline())) {
            TaskSpecification taskSpecification = new TaskSpecification(taskRequestFilter, professorService.getAuthProfessor().getCourses());
            result = taskMapper.toDtoListForViewAll(taskRepository.findAll(taskSpecification, pageable));
        } else {
            result = taskMapper.toDtoListForViewAll(taskRepository.findDistinctByCourseIn(professorService.getAuthProfessor().getCourses(), pageable));
        }
        log.info("TaskServiceImpl getAll finish");
        return result;
    }
    @Override
    public void deleteById(Long taskId) {
        log.info("TaskServiceImpl deleteById start");
        taskRepository.delete(getById(taskId));
        log.info("TaskServiceImpl deleteById finish");
    }
    @Override
    public Task getById(Long taskId) {
        log.info("TaskServiceImpl getById start");
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task with id = "+taskId+" not found")
        );
        courseService.isCourseAssignedToProfessor(task.getCourse().getId());
        log.info("TaskServiceImpl getById finish");
        return task;
    }

    @Override
    public TaskResponseForAdd getByIdForAdd(Long taskId) {
        log.info("TaskServiceImpl getByIdForAdd start");
        TaskResponseForAdd response = taskMapper.toDtoForAdd(getById(taskId));
        log.info("TaskServiceImpl getByIdForAdd finish");
        return response;
    }
    @Override
    @Transactional
    public void add(TaskRequestForAdd taskRequestForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("TaskServiceImpl add start");
        Task task = taskMapper.toEntityForAdd(taskRequestForAdd, this, courseService);
        if(taskRequestForAdd.getFile() != null)task.setFile(minioService.putMultipartFile(taskRequestForAdd.getFile()));
        save(task);
        log.info("TaskServiceImpl add finish");
    }
    @Override
    @Transactional
    public void save(Task task) {
        log.info("TaskServiceImpl save start");
        taskRepository.save(task);
        log.info("TaskServiceImpl save finish");
    }
    @Override
    public TaskResponseForView getByIdForView(Long id) {
        log.info("TaskServiceImpl getByIdForView start");
        TaskResponseForView response = taskMapper.toDtoForViewPage(getById(id));
        log.info("TaskServiceImpl getByIdForView finish");
        return response;
    }
    @Override
    public Map<String, String> getAllCloseForSelectByCourseId(Long courseId) {
        log.info("TaskServiceImpl getAllCloseForSelectByCourseId start");
        Map<String, String> forSelect = new HashMap<>();
        for (Task task : taskRepository.findAllByCourseIdAndStatus(courseId, StatusTask.CLOSE)) {
            forSelect.put(task.getId().toString(), task.getName());
        }
        log.info("TaskServiceImpl getAllCloseForSelectByCourseId finish");
        return forSelect;
    }
    @Override
    @Transactional
    public void open(TaskRequestForOpen taskRequestForOpen) {
        log.info("TaskServiceImpl open start");
        Task task = taskMapper.toEntityForOpen(taskRequestForOpen, this);
        save(task);
        studentsTaskService.triggerToOpenTask(task);
        log.info("TaskServiceImpl open finish");
    }
    @Override
    @Transactional
    public void close(List<Long> taskId) {
        log.info("TaskServiceImpl close start");
        for (Long id : taskId) {
            Task task = getById(id);
            task.setStatus(StatusTask.CLOSE);
            task.setDeadline(null);
            save(task);
        }
        log.info("TaskServiceImpl close finish");
    }
    @Override
    public Map<String, String> getAllOpenForSelectByCourseId(Long courseId) {
        log.info("TaskServiceImpl getAllOpenForSelectByCourseId start");
        Map<String, String> forSelect = new HashMap<>();
        for (Task task : taskRepository.findAllByCourseIdAndStatus(courseId, StatusTask.OPEN)) {
            forSelect.put(task.getId().toString(), task.getName());
        }
        log.info("TaskServiceImpl getAllOpenForSelectByCourseId finish");
        return forSelect;
    }
    @Override
    public Long countAllTask() {
        log.info("TaskServiceImpl countAllTask start");
        Long result = taskRepository.countAllByCourseIn(professorService.getAuthProfessor().getCourses());
        log.info("TaskServiceImpl countAllTask finish");
        return result;
    }
}