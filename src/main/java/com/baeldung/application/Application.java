package com.baeldung.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner init(UserRepository userRepository) {
//        return name -> {
//                User user = new User();
//                userRepository.save(user);
//
//            userRepository.findAll().forEach(System.out::println);
//        };
//    }
}
