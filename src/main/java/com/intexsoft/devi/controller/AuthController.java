package com.intexsoft.devi.controller;

import com.intexsoft.devi.controller.request.LoginForm;
import com.intexsoft.devi.controller.request.SignUpForm;
import com.intexsoft.devi.controller.response.JwtResponse;
import com.intexsoft.devi.entity.User;
import com.intexsoft.devi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for registration and authorization user
 *
 * @author ilya.korzhavin
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * Create and return token by name and password user
     *
     * @param loginRequest request user data
     * @return token
     */
    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody LoginForm loginRequest) {
        return userService.getToken(loginRequest);
    }

    /**
     * Create new user account and return it
     *
     * @param signUpRequest request user data
     * @return new user data
     */
    @PostMapping("/signup")
    public User registerUser(@RequestBody SignUpForm signUpRequest) {
        return userService.save(signUpRequest);
    }
}
