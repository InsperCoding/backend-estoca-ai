package com.estocaai.backend.Controller;

import com.estocaai.backend.Classes.Lista;
import com.estocaai.backend.Classes.Produto;
import com.estocaai.backend.Repository.ListaRepository;
import com.estocaai.backend.Repository.ProdutoRepository;
import com.estocaai.backend.User.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listas")
public class ListaController {

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserService userService;

    // Lista todas as listas
    @GetMapping
    public ResponseEntity<?> getAllListas(@RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        List<Lista> listas = listaRepository.findAll();
        return ResponseEntity.ok(listas);
    }

    // Cria uma nova lista
    @PostMapping
    public ResponseEntity<?> createLista(@RequestBody Lista lista,
                                         @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Lista novaLista = listaRepository.save(lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLista);
    }

    // Retorna os detalhes de uma lista específica
    @GetMapping("/{id}")
    public ResponseEntity<?> getListaById(@PathVariable String id,
                                          @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        return ResponseEntity.ok(lista);
    }

    // Remove uma lista
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLista(@PathVariable String id,
                                         @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        listaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Retorna os produtos de uma lista específica
    @GetMapping("/{id}/produtos")
    public ResponseEntity<?> getProdutosInLista(@PathVariable String id,
                                                @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        List<Produto> produtos = produtoRepository.findAllById(lista.getProdutosIds());
        return ResponseEntity.ok(produtos);
    }

    // Adiciona um produto a uma lista
    @PostMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> addProdutoToLista(@PathVariable String id,
                                               @PathVariable String id_produto,
                                               @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        lista.getProdutosIds().add(id_produto);
        Lista listaAtualizada = listaRepository.save(lista);
        return ResponseEntity.ok(listaAtualizada);
    }

    // Remove um produto de uma lista
    @DeleteMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> removeProdutoFromLista(@PathVariable String id,
                                                    @PathVariable String id_produto,
                                                    @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        lista.getProdutosIds().remove(id_produto);
        Lista listaAtualizada = listaRepository.save(lista);
        return ResponseEntity.ok(listaAtualizada);
    }
}
