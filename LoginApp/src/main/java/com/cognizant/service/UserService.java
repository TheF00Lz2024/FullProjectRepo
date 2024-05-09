package com.cognizant.service;

import com.cognizant.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getUser(User user);
}
