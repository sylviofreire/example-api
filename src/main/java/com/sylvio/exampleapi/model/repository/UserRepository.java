package com.sylvio.exampleapi.model.repository;

import com.sylvio.exampleapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
