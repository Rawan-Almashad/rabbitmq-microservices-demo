package iti.gov.consumer.service;


import iti.gov.consumer.dto.UserRegisteredEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class UserRegistrationListener {

    private MessageService messageService;


    @Autowired
    public UserRegistrationListener(MessageService messageService)
    {
        this.messageService=messageService;
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user-registration", durable = "true"),
            exchange = @Exchange(value = "Direct-Exchange", type = "direct"),
            key = "register"
    ))
    public void handleUserRegistration(UserRegisteredEvent message) {
        System.out.println("New user registered!");
        System.out.println("Name: " + message.getFirstname() + " " + message.getLastname());
        System.out.println("Username: " + message.getUsername());
        System.out.println("Registered at: " + message.getRegisteredAt());
        System.out.println("------------------------------------------------------");
        String content = "New user registered at  "+message.getRegisteredAt()+" with username = "+
                message.getUsername();
        messageService.saveMessage(content);
    }
}