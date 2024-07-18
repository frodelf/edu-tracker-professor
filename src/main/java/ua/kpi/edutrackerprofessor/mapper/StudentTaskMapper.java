package ua.kpi.edutrackerprofessor.mapper;

import ua.kpi.edutrackerprofessor.dto.studentTask.StudentTaskResponseForViewAll;
import ua.kpi.edutrackerentity.entity.StudentsTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

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
}