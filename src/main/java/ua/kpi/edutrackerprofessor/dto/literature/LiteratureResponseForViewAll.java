package ua.kpi.edutrackerprofessor.dto.literature;

import lombok.Data;

import java.util.Map;

@Data
public class LiteratureResponseForViewAll {
    private Long id;
    private String name;
    private String link;
    private Map<String, String> course;
}