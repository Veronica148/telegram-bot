package com.app.services;

import com.app.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserService{
    User getUserById(long id) throws ExecutionException, InterruptedException;
    Optional<User> getUserByIdOpt(long id) throws ExecutionException, InterruptedException;
    List<User> getAllUsers() throws ExecutionException, InterruptedException;
    User saveUser(User entity) throws ExecutionException, InterruptedException;
    boolean removeUser(long id);
}
