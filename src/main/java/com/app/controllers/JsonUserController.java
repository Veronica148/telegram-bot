package com.app.controllers;

import com.app.model.User;
import com.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class JsonUserController {

    @Autowired
    private UserService service;

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() throws ExecutionException, InterruptedException {
            return new ResponseEntity<>(service.getAllUsers(), OK);
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById (@PathVariable Long id) throws ExecutionException, InterruptedException {
        return service.getUserByIdOpt(id)
                    .map(user -> new ResponseEntity(List.of(user), OK))
                    .orElse(new ResponseEntity(NOT_FOUND));
    }
}
