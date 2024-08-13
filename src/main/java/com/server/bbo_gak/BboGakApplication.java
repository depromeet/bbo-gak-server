package com.server.bbo_gak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class BboGakApplication {

    public static void main(String[] args) {
        SpringApplication.run(BboGakApplication.class, args);
    }

}
