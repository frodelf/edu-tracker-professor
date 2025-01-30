package ua.kpi.edutrackerprofessor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnumControllerTest {

    private final EnumController enumController = new EnumController();

    @Test
    void getTaskStatus() {
        ResponseEntity<Map<String, String>> response = enumController.getTaskStatus();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);

        for (StatusTask status : StatusTask.values()) {
            assertTrue(responseBody.containsKey(status.name()));
            assertEquals(status.name(), responseBody.get(status.name()));
        }

        assertEquals(StatusTask.values().length, responseBody.size());
    }
}
