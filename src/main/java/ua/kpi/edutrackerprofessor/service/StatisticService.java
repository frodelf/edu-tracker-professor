package ua.kpi.edutrackerprofessor.service;

import java.util.Map;

public interface StatisticService {
    Map<String, String> getStatisticForStudent(Long courseId, Long studentId);
}