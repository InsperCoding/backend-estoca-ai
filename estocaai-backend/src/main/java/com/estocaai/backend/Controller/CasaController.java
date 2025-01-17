package com.estocaai.backend.Controller;

import com.estocaai.backend.Classes.Casa;
import com.estocaai.backend.Repository.CasaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.estocaai.backend.User.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/casas")
public class CasaController {

    @Autowired
    private CasaRepository casaRepository;

    @Autowired
    private UserService userService;

    // Lista todas as casas
    @GetMapping
    public ResponseEntity<?> getAllCasas(@RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        List<Casa> casas = casaRepository.findAll();
        return ResponseEntity.ok(casas);
    }

    // Retorna detalhes de uma casa específica
    @GetMapping("/{casaId}")
    public ResponseEntity<?> getCasaById(@PathVariable() String casaId,
                                         @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));
        return ResponseEntity.ok(casa);
    }

    // Cria uma nova casa
    @PostMapping
    public ResponseEntity<?> createCasa(@RequestHeader(value = "Authorization") String token,
                                        @RequestBody Casa casa) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa novaCasa = casaRepository.save(casa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCasa);
    }

    // Atualiza uma casa existente
    @PutMapping("/{casaId}")
    public ResponseEntity<?> updateCasa(@RequestHeader(value = "Authorization") String token,
                                        @PathVariable String casaId,
                                        @RequestBody Casa casaAtualizada) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));
        casa.setNome(casaAtualizada.getNome());
        casa.setEstado(casaAtualizada.getEstado());
        casa.setCidade(casaAtualizada.getCidade());
        casa.setBairro(casaAtualizada.getBairro());
        casa.setRua(casaAtualizada.getRua());
        casa.setNumero(casaAtualizada.getNumero());
        casa.setComplemento(casaAtualizada.getComplemento());
        Casa casaSalva = casaRepository.save(casa);
        return ResponseEntity.ok(casaSalva);
    }

    // Remove uma casa
    @DeleteMapping("/{casaId}")
    public ResponseEntity<?> deleteCasa(@RequestHeader(value = "Authorization") String token,
                                        @PathVariable String casaId) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        casaRepository.deleteById(casaId);
        return ResponseEntity.noContent().build();
    }
}