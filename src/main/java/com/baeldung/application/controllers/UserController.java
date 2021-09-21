package com.baeldung.application.controllers;

import com.baeldung.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;

    public UserController( UserService service) {
        this.service = service;
    }

//    @GetMapping("/users")
//    public List<User> getUsers() {
//        return (List<User>) userRepository.findAll();
//    }
//
//    @PostMapping("/users")
//    void addUser(@RequestBody User user) {
//        userRepository.save(user);
//    }

    @GetMapping("/users/{user}/{pass}/{issueKey}/{formNumber}/isAuthorized")
    public ResponseEntity<String> checkAuthorized(@PathVariable String user, @PathVariable String pass, @PathVariable String issueKey, @PathVariable String formNumber) {

        int res=service.userAuthorized(user,pass,issueKey,formNumber);
        if (res!=200){
            return new ResponseEntity<>(
                    String.valueOf(res),
                    HttpStatus.BAD_REQUEST);
        }else
            return new ResponseEntity<>(HttpStatus.OK);
    }

}
