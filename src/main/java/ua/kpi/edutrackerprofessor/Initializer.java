package ua.kpi.edutrackerprofessor;

import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerentity.entity.Professor;
import ua.kpi.edutrackerentity.entity.Student;
import ua.kpi.edutrackerprofessor.service.ProfessorService;
import ua.kpi.edutrackerprofessor.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {
    private final ProfessorService professorService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(professorService.count()==0){
            Professor professor = new Professor();
            professor.setLastName("Безпала");
            professor.setName("Ольга");
            professor.setMiddleName("Миколаївна");
            professor.setEmail("professor@gmail.com");
            professor.setPassword(new BCryptPasswordEncoder().encode("professor"));
            professor.setTelegram("@professor_tg");
            professor.setDegree("Старший викладач");
            professorService.save(professor);
        }
    }
}