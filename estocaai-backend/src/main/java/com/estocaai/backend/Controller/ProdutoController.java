package com.estocaai.backend.Controller;

import com.estocaai.backend.Classes.Produto;
import com.estocaai.backend.Repository.ProdutoRepository;
import com.estocaai.backend.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllProdutos(@RequestHeader(value = "Authorization") String token) {
        // Verifica se o token é válido
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        List<Produto> produtos = produtoRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    // Retorna os detalhes de um produto específico
    @GetMapping("/{id}")
    public ResponseEntity<?> getProdutoById(@PathVariable String id,
                                            @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return ResponseEntity.ok(produto);
    }

    // Cadastra um novo produto
    @PostMapping
    public ResponseEntity<?> createProduto(@RequestBody Produto produto,
                                           @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        Produto novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    // Remove um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable String id,
                                           @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
