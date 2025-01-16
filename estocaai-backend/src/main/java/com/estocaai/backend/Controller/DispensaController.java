package com.estocaai.backend.Controller;

import com.estocaai.backend.Repository.ProdutoRepository;
import com.estocaai.backend.Classes.Dispensa;
import com.estocaai.backend.Classes.Produto;
import com.estocaai.backend.Repository.DispensaRepository;
import com.estocaai.backend.User.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dispensas")
public class DispensaController {

    @Autowired
    private DispensaRepository dispensaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserService userService;

    // Lista todas as dispensas
    @GetMapping
    public ResponseEntity<?> getAllDispensas(@RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        List<Dispensa> dispensas = dispensaRepository.findAll();
        return ResponseEntity.ok(dispensas);
    }

    // Cria uma nova dispensa
    @PostMapping
    public ResponseEntity<?> createDispensa(@RequestHeader(value = "Authorization") String token,
                                            @RequestBody Dispensa dispensa) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Dispensa novaDispensa = dispensaRepository.save(dispensa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaDispensa);
    }

    // Retorna os detalhes de uma dispensa específica
    @GetMapping("/{id}")
    public ResponseEntity<?> getDispensaById(@PathVariable String id,
                                             @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        return ResponseEntity.ok(dispensa);
    }

    // Remove uma dispensa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDispensa(@PathVariable String id,
                                            @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        dispensaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Retorna os produtos de uma dispensa específica
    @GetMapping("/{id}/produtos")
    public ResponseEntity<?> getProdutosInDispensa(@PathVariable String id,
                                                   @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        List<Produto> produtos = produtoRepository.findAllById(dispensa.getProdutosIds());
        return ResponseEntity.ok(produtos);
    }

    // Adiciona um produto a uma dispensa
    @PostMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> addProdutoToDispensa(@PathVariable String id,
                                                  @PathVariable String id_produto,
                                                  @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        dispensa.getProdutosIds().add(id_produto);
        Dispensa dispensaAtualizada = dispensaRepository.save(dispensa);
        return ResponseEntity.ok(dispensaAtualizada);
    }

    // Remove um produto de uma dispensa
    @DeleteMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> removeProdutoFromDispensa(@PathVariable String id,
                                                       @PathVariable String id_produto,
                                                       @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        dispensa.getProdutosIds().remove(id_produto);
        Dispensa dispensaAtualizada = dispensaRepository.save(dispensa);
        return ResponseEntity.ok(dispensaAtualizada);
    }

    @PutMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> updateProdutoQuantidade(@RequestHeader(value = "Authorization") String token,
                                                     @PathVariable String id,
                                                     @PathVariable String id_produto,
                                                     @RequestParam int quantidade) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }

        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));

        int index = dispensa.getProdutosIds().indexOf(id_produto);
        if (index == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado na dispensa!");
        }

        if (quantidade <= 0) {
            dispensa.getProdutosIds().remove(index);
            dispensa.getProdutosQuantidades().remove(index);
        } else {
            dispensa.getProdutosQuantidades().set(index, quantidade);
        }

        Dispensa dispensaAtualizada = dispensaRepository.save(dispensa);

        return ResponseEntity.ok(dispensaAtualizada);
    }

}
