package com.intexsoft.devi.service.interfaces;

import com.intexsoft.devi.controller.request.UserAuthParameters;
import com.intexsoft.devi.controller.request.UserRegistrationParameters;
import com.intexsoft.devi.controller.response.JwtResponse;
import com.intexsoft.devi.entity.User;

/**
 * Service for class {@link User}
 *
 * @author ilya.korzhavin
 */
public interface UserService {
    User get(String username);

    User save(UserRegistrationParameters signUpRequest);

    JwtResponse getToken(UserAuthParameters loginRequest);

    JwtResponse refreshToken(String token);
}
