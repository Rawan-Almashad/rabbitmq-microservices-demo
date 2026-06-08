package iti.gov.producer.controller;

import iti.gov.producer.dto.UserRegisteredEvent;
import iti.gov.producer.entity.User;
import iti.gov.producer.service.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    private UserService userService;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RegistrationController(UserService userService , RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
    }
    @GetMapping("/")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            User savedUser = userService.registerUser(user);

            UserRegisteredEvent message = new UserRegisteredEvent(
                    (long) savedUser.getId(),
                    savedUser.getFirstname(),
                    savedUser.getLastname(),
                    savedUser.getUsername(),
                    LocalDateTime.now()
            );

            rabbitTemplate.convertAndSend("user-registration", message);

            model.addAttribute("success", true);
            model.addAttribute("message", "User registered successfully!");

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("user", new User());
        return "register";
    }
}
