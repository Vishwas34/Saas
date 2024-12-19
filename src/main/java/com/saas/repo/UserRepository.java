package com.saas.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saas.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    
}
