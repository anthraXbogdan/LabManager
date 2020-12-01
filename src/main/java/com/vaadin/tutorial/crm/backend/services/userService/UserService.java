package com.vaadin.tutorial.crm.backend.services.userService;

import com.vaadin.tutorial.crm.backend.entities.User;

import java.util.List;

public interface UserService {

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    void saveUser(User user);

    void deleteUser(User user);

    List<User> findAll();

}
