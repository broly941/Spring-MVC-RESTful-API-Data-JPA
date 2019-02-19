package com.intexsoft.devi.service;

import com.intexsoft.devi.entity.User;

/**
 * Service for class {@link com.intexsoft.devi.entity.User}
 *
 * @author ilya.korzhavin
 */
public interface UserService {
    void save (User user);
    User findByUserName(String username);
}
