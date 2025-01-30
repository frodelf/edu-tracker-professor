package ua.kpi.edutrackerprofessor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ua.kpi.edutrackerprofessor.dto.MessageStudentClick;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PresentControllerImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private PresentControllerImpl presentController;

    @Mock(lenient = true)
    private ObjectMapper objectMapper;

    @Test
    void receiveMessageFromQueue() throws JsonProcessingException {
        String message = "{\"studentId\": 1, \"lessonId\": 2}";
        MessageStudentClick messageStudentClick = new MessageStudentClick();
        messageStudentClick.setStudentId(1L);
        messageStudentClick.setLessonId(2L);

        when(objectMapper.readValue(message, MessageStudentClick.class)).thenReturn(messageStudentClick);

        presentController.receiveMessageFromQueue(message);

        verify(messagingTemplate, times(1)).convertAndSend("/topic/lesson-2", 1L);
    }
}
