package iti.gov.consumer.controller;

import iti.gov.consumer.entity.Message;
import iti.gov.consumer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/consumer")
public class MessageController {
    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService)
    {
     this.messageService=messageService;
    }
    @GetMapping("/")
    public String viewMessages(Model model) {
        List<Message> messages = messageService.getAllMessages();
        model.addAttribute("messages", messages);
        return "messages";
    }
}
