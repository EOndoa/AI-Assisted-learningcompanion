package com.etienne.AI_Assisted_learningcompanion.service;

import com.etienne.AI_Assisted_learningcompanion.model.User;
import com.etienne.AI_Assisted_learningcompanion.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {

        user.setRole("STUDENT");
        user.setEnabled(false);
        user.setVerificationToken(UUID.randomUUID().toString());

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User verifyAccount(String token) {

        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            return null;
        }

        user.setEnabled(true);
        user.setVerificationToken(null);

        return userRepository.save(user);
    }

    public List<User> getAllStudents() {

        return userRepository.findAll()
                .stream()
                .filter(user ->
                        "STUDENT".equalsIgnoreCase(user.getRole()))
                .toList();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void verifyUser(Long id) {

        User user = getUserById(id);

        if (user != null) {
            user.setEnabled(true);
            user.setVerificationToken(null);
            userRepository.save(user);
        }
    }

    public void disableUser(Long id) {

        User user = getUserById(id);

        if (user != null) {
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    public void makeAdmin(Long id) {

        User user = getUserById(id);

        if (user != null) {
            user.setRole("ADMIN");
            userRepository.save(user);
        }
    }

    public void makeStudent(Long id) {

        User user = getUserById(id);

        if (user != null) {
            user.setRole("STUDENT");
            userRepository.save(user);
        }
    }
}