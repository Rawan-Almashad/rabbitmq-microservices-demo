package iti.gov.frontend.client;

import iti.gov.frontend.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "producer")
public interface ProducerClient {

    @PostMapping("/producer/register")
    String register(@RequestBody User user);

}