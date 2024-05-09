package com.cognizant.config;

import java.util.Map;
import com.cognizant.model.User;
public interface JWTTokenGenerator {
    Map<String, String> generateToken(User user);
}
