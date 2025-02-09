package com.estocaai.backend.User.Controller;

import com.estocaai.backend.User.Model.User;
import com.estocaai.backend.User.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.estocaai.backend.User.Service.UserService;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user.getEmail(), user.getPassword(), user.getName());
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword());
    }

    @GetMapping("/logout")
    public void logout(@RequestBody User user) {
        userService.logout(user.getToken());
    }

    @PutMapping("/users/{id}/foto")
    public ResponseEntity<?> atualizarFotoUsuario(@PathVariable String id,
                                                  @RequestBody String base64Foto,
                                                  @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }

        User userAtualizado = userService.atualizarFotoPerfil(id, base64Foto);
        return ResponseEntity.ok(userAtualizado);
    }

    @GetMapping("/users/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }

        Optional<User> userOpt = userService.findByToken(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        User user = userOpt.get();
        return ResponseEntity.ok(new UserResponseDTO(user.getName(), user.getEmail(), user.getFotoPerfil()));
    }

    @PutMapping("/users/details")
    public ResponseEntity<?> updateUserDetails(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody UserResponseDTO updatedUser) {

        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }

        try {
            User savedUser = userService.updateUserDetails(token, updatedUser.getName(), updatedUser.getEmail());
            return ResponseEntity.ok(new UserResponseDTO(savedUser.getEmail(), savedUser.getFotoPerfil(), savedUser.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
