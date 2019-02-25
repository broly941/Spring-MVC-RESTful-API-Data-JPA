package com.intexsoft.devi.service;

import com.intexsoft.devi.controller.request.LoginForm;
import com.intexsoft.devi.controller.request.SignUpForm;
import com.intexsoft.devi.controller.response.JwtResponse;
import com.intexsoft.devi.entity.User;

/**
 * Service for class {@link User}
 *
 * @author ilya.korzhavin
 */
public interface UserService {
    User get(String username);

    User save(SignUpForm signUpRequest);

    JwtResponse getToken(LoginForm loginRequest);

    JwtResponse refreshToken(String token);
}
