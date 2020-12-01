package com.vaadin.tutorial.crm.backend.services.userService;

import com.vaadin.tutorial.crm.backend.entities.User;
import com.vaadin.tutorial.crm.backend.repositories.RoleRepository;
import com.vaadin.tutorial.crm.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        if (!user.getUsername().equals("Admin")) {
            userRepository.delete(user);
        }

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
