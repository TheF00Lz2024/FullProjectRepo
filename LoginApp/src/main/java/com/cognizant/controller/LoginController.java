package com.cognizant.controller;

import com.cognizant.config.JWTTokenGenerator;
import com.cognizant.exception.DuplicateEmailException;
import com.cognizant.exception.NoSuchUser;
import com.cognizant.model.UserConfig;
import com.cognizant.model.User;
import com.cognizant.service.UserService;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/v1")
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    UserService userService;
    JWTTokenGenerator jwtTokenGenerator;
    //construct userService and JWT Token
    @Autowired
    public LoginController(UserService userService, JWTTokenGenerator jwtTokenGenerator) {
        this.userService = userService;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }
    // API for user account login at login page
    @GetMapping("/login")
    public ResponseEntity<Map<String,String>> getUserAccount(@RequestParam("username") String username, @RequestParam("password") String password){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(UserConfig.class);
        User createUser = applicationContext.getBean("User", User.class);
        createUser.setUsername(username);
        // hash the user password via SHA256
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        createUser.setPassword(hashedPassword);
        try{
            //get the found user account
            User foundUser = userService.getUser(createUser).get(0);
            // pass the found user obj and produce a response with the JWT token
            return new ResponseEntity<>(jwtTokenGenerator.generateToken(foundUser), HttpStatus.OK);
        }catch (NoSuchUser err){
            throw (err);
        }
    }
    // API for creating user account at sign up page
    @PostMapping("/login")
    public ResponseEntity<User> userCreateAccount(@RequestBody User user){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(UserConfig.class);
        User createUser = applicationContext.getBean("User", User.class);
        createUser.setUsername(user.getUsername());
        // encrypt the user pass via SHA 256
        String hashedPassword = Hashing.sha256()
                .hashString(user.getPassword(), StandardCharsets.UTF_8)
                .toString();
        // set the encrypted password to the user obj
        createUser.setPassword(hashedPassword);
        try{
            // pass the new user obj to User Service o store it in DB and send back a response
            return new ResponseEntity<>(userService.saveUser(createUser), HttpStatus.CREATED);
        }catch (DuplicateEmailException err){
            throw (err);
        }
    }
    // Exception handler when no such Username/ invalid username and password
    @ExceptionHandler(value=NoSuchUser.class)
    public ResponseEntity<String> invalidUser(NoSuchUser message){
        logger.error(message.getMessage());
        return new ResponseEntity<>(message.getMessage(), HttpStatus.NOT_FOUND);
    }
    // Exception handling for duplicate email is being sued or sign up
    @ExceptionHandler(value=DuplicateEmailException.class)
    public ResponseEntity<String> duplicateEmail(DuplicateEmailException message){
        logger.error(message.getMessage());
        return new ResponseEntity<>(message.getMessage(), HttpStatus.FORBIDDEN);
    }


}
