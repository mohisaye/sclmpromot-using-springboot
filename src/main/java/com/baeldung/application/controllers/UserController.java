package com.baeldung.application.controllers;

import com.baeldung.application.entities.User;
import com.baeldung.application.repositories.UserRepository;
import com.baeldung.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;
    private final UserService service;

    public UserController(UserRepository userRepository, UserService service) {
        this.userRepository = userRepository;
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @PostMapping("/users")
    void addUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping("/users/{user}/{pass}/{issueKey}/isAuthorized")
    public ResponseEntity<String> checkAuthorized(@PathVariable String user, @PathVariable String pass,@PathVariable String issueKey) {
        User user1=new User(user,issueKey) ;
        userRepository.save(user1);
        int res=service.userAuthorized(user,pass);
        if (res!=200){
            return new ResponseEntity<>(
                    String.valueOf(res),
                    HttpStatus.BAD_REQUEST);
        }else
            return new ResponseEntity<>(HttpStatus.OK);
    }

}
