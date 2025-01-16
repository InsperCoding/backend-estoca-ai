package com.estocaai.backend.User.Controller;
import com.estocaai.backend.User.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.estocaai.backend.User.Service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user.getEmail(), user.getPassword());
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

}
