package ru.otus.hw.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw.front.FrontendService;

@Slf4j
@Controller
public class MessageController {

    private final FrontendService frontendService;
    private SimpMessagingTemplate template;

    public MessageController(FrontendService frontendService, SimpMessagingTemplate template) {
        this.frontendService = frontendService;
        this.template = template;
    }

    @MessageMapping("/createUser")
    public void userSave(String saveMessage) {
        log.info("got saveMessage:{}", saveMessage);
        frontendService.saveUserData(saveMessage, userData -> template.convertAndSend("/topic/createUser", userData));
    }

}
