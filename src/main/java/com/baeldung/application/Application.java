package com.baeldung.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner init(PromoteService promoteService, PromoteRepository repository) {
//        return args -> {
//            List<Promote> promotes = new ArrayList<>();
//            for (int i = 0; i < 30; i++) {
//                Load load = new Load("as" + String.valueOf(i), "34", "55", "bmi", "a.d", "f.g", "arch");
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                Date date = new Date();
//                promoteService.savePromote(String.valueOf(i+20),load,"ttreee");
////                Promote promote = new Promote(String.valueOf(i+20),"jjjddj",formatter.format(date),load);
////                repository.save(promote);
//            }
//        };
//    }
}
