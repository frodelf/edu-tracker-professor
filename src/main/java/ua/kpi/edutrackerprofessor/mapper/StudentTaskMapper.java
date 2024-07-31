package ua.kpi.edutrackerprofessor.mapper;

import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForLessonEdit;
import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerentity.entity.StudentsTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class StudentTaskMapper {
    public StudentTaskResponseForViewAll toDtoForViewAll(StudentsTask studentsTask){
        StudentTaskResponseForViewAll studentTaskResponseForViewAll = new StudentTaskResponseForViewAll();
        studentTaskResponseForViewAll.setId(studentsTask.getId());
        studentTaskResponseForViewAll.setMark(studentsTask.getMark());
        studentTaskResponseForViewAll.setStatus(studentsTask.getStatus());
        if(studentsTask.getStudent()!=null) {
            studentTaskResponseForViewAll.setGroupName(studentsTask.getStudent().getGroupName());
            studentTaskResponseForViewAll.setFullName(studentsTask.getStudent().getLastName() + " " + studentsTask.getStudent().getName());
            studentTaskResponseForViewAll.setTelegram(studentsTask.getStudent().getTelegram());
            studentTaskResponseForViewAll.setStudentId(studentsTask.getStudent().getId());
        }
        if(studentsTask.getMyWork()!=null){
            studentTaskResponseForViewAll.setMyWork(studentsTask.getMyWork());
        }
        return studentTaskResponseForViewAll;
    }
    public Page<StudentTaskResponseForViewAll> toDtoListForViewAll(Page<StudentsTask> studentsTasks){
        return new PageImpl<>(studentsTasks.getContent().stream()
                .map(this::toDtoForViewAll)
                .collect(Collectors.toList()), studentsTasks.getPageable(), studentsTasks.getTotalElements());
    }
    public List<StudentTaskResponseForLessonEdit> toDtoListForLessonAdd(List<StudentsTask> studentTasks) {
        List<StudentTaskResponseForLessonEdit> studentTaskResponseForLessonEdit = new ArrayList<>();
        for (StudentsTask studentsTask : studentTasks) {
            studentTaskResponseForLessonEdit.add(toDtoForLessonAdd(studentsTask));
        }
        return studentTaskResponseForLessonEdit;
    }
    private StudentTaskResponseForLessonEdit toDtoForLessonAdd(StudentsTask studentsTask) {
        StudentTaskResponseForLessonEdit studentTaskResponseForLessonEdit = new StudentTaskResponseForLessonEdit();
        studentTaskResponseForLessonEdit.setId(studentsTask.getId());
        studentTaskResponseForLessonEdit.setStatus(studentsTask.getStatus());
        studentTaskResponseForLessonEdit.setMark(studentsTask.getMark());
        studentTaskResponseForLessonEdit.setMyWork(studentsTask.getMyWork());
        if(nonNull(studentsTask.getTask())){
            studentTaskResponseForLessonEdit.setTaskName(studentsTask.getTask().getName());
            studentTaskResponseForLessonEdit.setTaskId(studentsTask.getTask().getId());
        }if(nonNull(studentsTask.getStudent())){
            studentTaskResponseForLessonEdit.setStudentId(studentsTask.getStudent().getId());
        }
        return studentTaskResponseForLessonEdit;
    }
}