package com.estocaai.backend.User.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estocaai.backend.User.model.User;
import com.estocaai.backend.User.repository.UserRepository;

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

            return loginToken + " " + user.getId();
        } else {
            return "Senha incorreta!";
        }
    }

    public User createUser(String email, String password, String name) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setName(name);
        user.setFotoPerfil(null);
        userRepository.save(user);
        return user;
    }

    public void logout(String token) {
        Optional<User> userOpt = userRepository.findByToken(token);

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();
        user.setToken(null);
        userRepository.save(user);
    }

    public User atualizarFotoPerfil(String userId, String base64Foto) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            user.setFotoPerfil(base64Foto);

            return userRepository.save(user);
        }

    public boolean isTokenValid(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        Optional<User> userOpt = userRepository.findByToken(token);
        return userOpt.isPresent();
    }

    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }


    public String getUsuarioIdFromToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        Optional<User> userOpt = userRepository.findByToken(token);
        return userOpt.map(User::getId).orElse(null);
    }

    public User escolherCasa(String userId, String casaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setCasaEscolhida(casaId);

        return userRepository.save(user);
    }

    public User updateUserDetails(String token, String newName, String newEmail) {
        Optional<User> userOpt = userRepository.findByToken(token);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado ou token inválido");
        }

        User user = userOpt.get();
        user.setName(newName);
        user.setEmail(newEmail);

        return userRepository.save(user);
    }

}
