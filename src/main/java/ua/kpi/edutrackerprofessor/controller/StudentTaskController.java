package ua.kpi.edutrackerprofessor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskRequestForFilter;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerprofessor.service.StudentsTaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student-task")
public class StudentTaskController {
    private final StudentsTaskService studentsTaskService;
    @GetMapping("/get-all-by-task")
    public ResponseEntity<Page<StudentTaskResponseForViewAll>> getAllStudentByTask(@ModelAttribute @Valid StudentTaskRequestForFilter studentTaskRequestForFilter) {
        return ResponseEntity.ok(studentsTaskService.getAll(studentTaskRequestForFilter));
    }
}