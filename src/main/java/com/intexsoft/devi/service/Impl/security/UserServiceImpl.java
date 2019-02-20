package com.intexsoft.devi.service.Impl.security;

import com.intexsoft.devi.controller.request.LoginForm;
import com.intexsoft.devi.controller.request.SignUpForm;
import com.intexsoft.devi.controller.response.JwtResponse;
import com.intexsoft.devi.entity.Role;
import com.intexsoft.devi.entity.User;
import com.intexsoft.devi.exception.ValidationException;
import com.intexsoft.devi.repository.RoleRepository;
import com.intexsoft.devi.repository.UserRepository;
import com.intexsoft.devi.security.JwtProvider;
import com.intexsoft.devi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link UserService} interface.
 * <p>
 * Create and manage of user entities
 *
 * @author ilya.korzhavin
 */
@Service
public class UserServiceImpl implements UserService {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PM = "ROLE_PM";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ADMIN_ROLE_NOT_FIND = "Admin Role not find";
    public static final String PM_ROLE_NOT_FIND = "PM Role not find";
    public static final String USER_ROLE_NOT_FIND = "User Role not find";
    public static final String ADMIN = "admin";
    public static final String PM = "pm";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * method return user entity
     *
     * @param username of entity
     * @return user entity
     */
    @Override
    public User get(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * method registry new user and return it
     *
     * @param signUpRequest request user data
     * @return new user
     */
    @Override
    @Transactional
    public User save(SignUpForm signUpRequest) {
        validationRequestData(signUpRequest);
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), getRoles(signUpRequest.getRole()));
        return userRepository.save(user);
    }

    /**
     * method create token by user
     *
     * @param loginRequest request user data
     * @return token
     */
    @Override
    public JwtResponse getToken(LoginForm loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityContext o = SecurityContextHolder.getContext();
        String jwt = jwtProvider.generateJwtToken(authentication);

        return new JwtResponse(jwt);
    }

    private Set<Role> getRoles(Set<String> requestRoles) {
        Set<Role> userRoles = new HashSet<>();
        requestRoles.forEach(role -> {
            switch (role.toLowerCase()) {
                case ADMIN:
                    Role adminRole = roleRepository.findByName(ROLE_ADMIN).orElseThrow(() -> new ValidationException(ADMIN_ROLE_NOT_FIND));
                    userRoles.add(adminRole);
                    break;
                case PM:
                    Role pmRole = roleRepository.findByName(ROLE_PM).orElseThrow(() -> new ValidationException(PM_ROLE_NOT_FIND));
                    userRoles.add(pmRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new ValidationException(USER_ROLE_NOT_FIND));
                    userRoles.add(userRole);
            }
        });
        return userRoles;
    }

    private void validationRequestData(SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ValidationException("Username is already taken");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ValidationException("Email is already in use");
        }
    }

}
