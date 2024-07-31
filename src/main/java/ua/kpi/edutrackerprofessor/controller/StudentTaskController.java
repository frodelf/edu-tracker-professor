package ua.kpi.edutrackerprofessor.controller;

import io.minio.errors.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForLessonEdit;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student-task")
public class StudentTaskController {
    private final StudentsTaskService studentsTaskService;
    @GetMapping("/get-all-by-task")
    public ResponseEntity<Page<StudentTaskResponseForViewAll>> getAllStudentByTask(@ModelAttribute @Valid StudentTaskRequestForFilter studentTaskRequestForFilter) {
        return ResponseEntity.ok(studentsTaskService.getAll(studentTaskRequestForFilter));
    }
    @PutMapping("/cancel-mark")
    public ResponseEntity<String> cancelMark(@RequestParam Long studentTaskId) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        studentsTaskService.cancelMark(studentTaskId);
        return ResponseEntity.ok("updated");
    }
    @PutMapping("/evaluate")
    public ResponseEntity<String> evaluate(@RequestParam Long studentTaskId, @RequestParam Double mark){
        studentsTaskService.evaluate(studentTaskId, mark);
        return ResponseEntity.ok("updated");
    }
    @GetMapping("/get-all-for-lesson-edit")
    public ResponseEntity<List<StudentTaskResponseForLessonEdit>> getAllForLessonEdit(@RequestParam Long studentId, @RequestParam Long lessonId){
        return ResponseEntity.ok(studentsTaskService.getAllByStudentIdAndLessonId(studentId, lessonId));
    }
}