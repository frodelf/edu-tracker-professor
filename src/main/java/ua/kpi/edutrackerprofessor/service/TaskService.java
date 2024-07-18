package ua.kpi.edutrackerprofessor.service;

import io.minio.errors.*;
import ua.kpi.edutrackerprofessor.dto.task.*;
import ua.kpi.edutrackerentity.entity.Task;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface TaskService {
    long countAllTasksByCourseId(long courseId);
    long countAllOpenTasksByCourseId(long courseId);
    long countAllCloseTasksByCourseId(long courseId);
    Page<TaskResponseViewAll> getAll(TaskRequestFilter taskRequestFilter);
    void deleteById(Long taskId);
    Task getById(Long taskId);
    TaskResponseForAdd getByIdForAdd(Long taskId);
    void add(TaskRequestForAdd taskRequestForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    void save(Task task);
    TaskResponseForView getByIdForView(Long id);
}