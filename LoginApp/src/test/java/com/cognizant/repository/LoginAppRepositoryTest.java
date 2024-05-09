package com.cognizant.repository;
import com.cognizant.model.User;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoginAppRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .username("Test@gmail.com")
                .password("123456789")
                .build();
    }

    @Test
    void shouldHaveUser(){
        String hashedPassword = Hashing.sha256()
                .hashString(user.getPassword(), StandardCharsets.UTF_8)
                .toString();
        List<User>foundUser = userRepository.loginUser(user.getUsername(),hashedPassword);
        assertEquals(1,foundUser.size());
    }
}
