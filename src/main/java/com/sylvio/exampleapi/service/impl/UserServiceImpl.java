package com.sylvio.exampleapi.service.impl;

import com.sylvio.exampleapi.model.entity.User;
import com.sylvio.exampleapi.model.repository.UserRepository;
import com.sylvio.exampleapi.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }
}
