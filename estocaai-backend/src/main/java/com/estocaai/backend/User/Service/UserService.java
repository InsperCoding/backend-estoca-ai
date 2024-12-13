package com.estocaai.backend.User.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estocaai.backend.User.Model.User;
import com.estocaai.backend.User.Repository.UserRepository;

import java.util.Optional;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "Usuário não encontrado";
        }

        User user = userOpt.get();
        boolean passwordMatches = BCrypt.checkpw(rawPassword, user.getPassword());

        if (passwordMatches) {
            String loginToken = UUID.randomUUID().toString();

            user.setToken(loginToken);
            userRepository.save(user);

            return loginToken;
        } else {
            return "Senha incorreta!";
        }
    }

    public User createUser(String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return user;
    }
}
