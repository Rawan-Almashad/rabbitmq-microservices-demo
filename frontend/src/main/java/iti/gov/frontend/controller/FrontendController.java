package iti.gov.frontend.controller;

import iti.gov.frontend.client.ProducerClient;
import iti.gov.frontend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/frontend")
public class FrontendController {


    private ProducerClient producerClient;

    public FrontendController(ProducerClient producerClient)
    {
        this.producerClient = producerClient;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            String response =producerClient.register(user);
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
        model.addAttribute("user", new User());
        return "register";
    }
}