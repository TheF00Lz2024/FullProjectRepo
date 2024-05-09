package com.cognizant.service;

import com.cognizant.exception.DuplicateEmailException;
import com.cognizant.exception.NoSuchUser;
import com.cognizant.model.User;
import com.cognizant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.findById(user.getUsername()).isEmpty()){
            userRepository.save(user);
            return user;
        } else{
            throw new DuplicateEmailException("{ \"errorMessage\": \"Email Address taken!\"}");
        }
    }

    @Override
    public List<User> getUser(User user) {
        Optional<User> findUser = userRepository.findById(user.getUsername());
        if (findUser.isEmpty()){
            throw new NoSuchUser("{\"errorMessage\": \"Username not found!\"}");
        } else{
            List<User> validUser = userRepository.loginUser(user.getUsername(), user.getPassword());
            if (validUser.isEmpty()){
                throw new NoSuchUser("{ \"errorMessage\": \"Please try again! Invalid email or password\"}");
            } else{
                return validUser;
            }
        }
    }
}
