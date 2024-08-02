package ua.kpi.edutrackerprofessor.controller;

import io.minio.errors.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.dto.task.TaskRequestFilter;
import ua.kpi.edutrackerprofessor.dto.task.TaskRequestForAdd;
import ua.kpi.edutrackerprofessor.dto.task.TaskResponseForAdd;
import ua.kpi.edutrackerprofessor.dto.task.TaskResponseViewAll;
import ua.kpi.edutrackerprofessor.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.kpi.edutrackerprofessor.dto.task.TaskRequestForOpen;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    @GetMapping({"", "/"})
    public ModelAndView index() {
        return new ModelAndView("task/index");
    }
    @GetMapping("/{id}")
    public ModelAndView viewPage(@PathVariable("id") Long id) {
        return new ModelAndView("task/view", "task", taskService.getByIdForView(id));
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<TaskResponseViewAll>> getAll(@ModelAttribute @Valid TaskRequestFilter taskRequestFilter){
        return ResponseEntity.ok(taskService.getAll(taskRequestFilter));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Long taskId){
        taskService.deleteById(taskId);
        return ResponseEntity.ok("deleted");
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<TaskResponseForAdd> getById(@RequestParam Long taskId){
        return ResponseEntity.ok(taskService.getByIdForAdd(taskId));
    }
    @PostMapping("/add")
    public ResponseEntity<String> add(@ModelAttribute @Valid TaskRequestForAdd taskRequestForAdd) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        taskService.add(taskRequestForAdd);
        return ResponseEntity.ok("saved");
    }
    @GetMapping("/get-all-close-for-select-by-course-id/{courseId}")
    public ResponseEntity<Map<String, String>> getAllCloseForSelectByCourseId(@PathVariable Long courseId){
        return ResponseEntity.ok(taskService.getAllCloseForSelectByCourseId(courseId));
    }
    @GetMapping("/get-all-open-for-select-by-course-id/{courseId}")
    public ResponseEntity<Map<String, String>> getAllOpenForSelectByCourseId(@PathVariable Long courseId){
        return ResponseEntity.ok(taskService.getAllOpenForSelectByCourseId(courseId));
    }
    @PostMapping("/open")
    public ResponseEntity<String> open(@ModelAttribute @Valid TaskRequestForOpen taskRequestForOpen){
        taskService.open(taskRequestForOpen);
        return ResponseEntity.ok("opened");
    }
    @PostMapping("/close")
    public ResponseEntity<String> close(@RequestParam @NotNull(message = "{error.field.empty}") List<Long> taskForClose){
        taskService.close(taskForClose);
        return ResponseEntity.ok("opened");
    }
    @GetMapping("/get-count-task")
    public ResponseEntity<Long> getCountTask(){
        return ResponseEntity.ok(taskService.countAllTask());
    }
    @ModelAttribute
    public void activeMenuItem(Model model) {
        model.addAttribute("taskActive", true);
    }
}