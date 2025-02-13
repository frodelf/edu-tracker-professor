package ua.kpi.edutrackerprofessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "ua.kpi.edutrackerentity.entity")
public class EduTrackerProfessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduTrackerProfessorApplication.class, args);
    }
}