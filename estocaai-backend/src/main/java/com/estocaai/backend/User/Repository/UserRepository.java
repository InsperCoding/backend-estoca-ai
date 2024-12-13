package com.estocaai.backend.User.Repository;

import com.estocaai.backend.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String Email);
}

