package ua.kpi.edutrackerprofessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ua.kpi.edutrackerentity", "ua.kpi.edutrackerprofessor"})
//TODO прибрати перевірку авторизації на клік, і додати цю перевірку на кожен запит(просто обробити 401 помилку)
public class EduTrackerProfessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduTrackerProfessorApplication.class, args);
    }
}