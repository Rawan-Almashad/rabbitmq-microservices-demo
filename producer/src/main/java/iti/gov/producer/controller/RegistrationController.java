package iti.gov.producer.controller;

import iti.gov.producer.dto.UserRegisteredEvent;
import iti.gov.producer.entity.User;
import iti.gov.producer.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/producer")
public class RegistrationController {

    private UserService userService;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RegistrationController(UserService userService,
                                  RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody User user) {

        User savedUser = userService.registerUser(user);

        UserRegisteredEvent message =
                new UserRegisteredEvent(
                        (long) savedUser.getId(),
                        savedUser.getFirstname(),
                        savedUser.getLastname(),
                        savedUser.getUsername(),
                        LocalDateTime.now()
                );

        rabbitTemplate.convertAndSend("Direct-Exchange",
                "register",
                message);

        return ResponseEntity.ok(
                "User registered successfully!");
    }
}
