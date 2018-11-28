package io.pivotal.pal.tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class WelcomeController {

    @Value("${WELCOME_MESSAGE}")
    private ;String helloMsg = "";

    @GetMapping("/")
    public String sayHello() {
        return helloMsg;
    }
}