package ua.kpi.edutrackerprofessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ua.kpi.edutrackerentity", "ua.kpi.edutrackerprofessor"})
public class EduTrackerProfessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduTrackerProfessorApplication.class, args);
    }

}
