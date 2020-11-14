package com.sylvio.exampleapi.service;

import com.sylvio.exampleapi.model.entity.User;

import java.util.Optional;

public interface UserService {
    User save(User any);

    Optional<User> getByID(Long id);

    void delete(User user);
}
