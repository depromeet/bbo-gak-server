package com.server.bbo_gak.global;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/error")
    public String generateError() {
        throw new RuntimeException("This is a test exception for Sentry!");
    }
}
