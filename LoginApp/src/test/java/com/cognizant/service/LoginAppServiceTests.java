package com.cognizant.service;

import com.cognizant.model.User;
import com.cognizant.repository.UserRepository;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoginAppServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .username("NewUser@gmail.com")
                .password("NewUser2000")
                .build();
    }
    @Test
    void addNewUser(){
        User saveUser = userService.saveUser(user);
        assert(saveUser.getPassword().equals(user.getPassword()));
        assert(saveUser.getUsername().equals(user.getUsername()));
    }
}
