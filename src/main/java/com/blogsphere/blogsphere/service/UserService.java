package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.UpdateProfileRequest;
import com.blogsphere.blogsphere.dto.UserResponse;
import com.blogsphere.blogsphere.entity.User;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return new UserResponse(user);
    }

    public void resetPassword(
            String email,
            String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);
    }

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException(
                    "Email already registered"
            );
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    public UserResponse updateProfile(
            UpdateProfileRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());

        User updatedUser =
                userRepository.save(user);

        return new UserResponse(updatedUser);
    }
}