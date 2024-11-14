package me.dyacode.chat_with_kafka.domain.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/")
    public String main() {

        return "index";
    }
}
