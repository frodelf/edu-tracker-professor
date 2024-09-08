package ua.kpi.edutrackerprofessor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.kpi.edutrackerprofessor.dto.MessageStudentClick;

@Component
@RequiredArgsConstructor
public class PresentControllerImpl {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String QUEUE_NAME = "queue";
    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessageFromQueue(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageStudentClick messageStudentClick = objectMapper.readValue(message, MessageStudentClick.class);
        messagingTemplate.convertAndSend("/topic/lesson-"+messageStudentClick.getLessonId(), messageStudentClick.getStudentId());
    }
}