package iti.gov.frontend.controller;

import iti.gov.frontend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class FrontendController {
    private final RestTemplate restTemplate;

    @Autowired
    public FrontendController(
            RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String showForm(Model model) {

        model.addAttribute("user",
                new User());

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            Model model) {

        try {

            String response =
                    restTemplate.postForObject(
                            "http://localhost:8080/producer/register",
                            user,
                            String.class);

            model.addAttribute(
                    "success",
                    true);

            model.addAttribute(
                    "message",
                    response);

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    e.getMessage());
        }

        model.addAttribute("user",
                new User());

        return "register";
    }
}
