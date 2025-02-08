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

    @GetMapping
    public ResponseEntity<?> getAllListas(@RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        List<Lista> listas = listaRepository.findAll();
        return ResponseEntity.ok(listas);
    }

    @PostMapping
    public ResponseEntity<?> createLista(@RequestBody Lista lista,
                                         @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        String usuarioId = userService.getUsuarioIdFromToken(token);

        if (listaRepository.findByUsuarioId(usuarioId).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O usuário já possui uma lista.");
        }

        lista.setUsuarioId(usuarioId);
        Lista novaLista = listaRepository.save(lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getListaById(@PathVariable String id,
                                          @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLista(@PathVariable String id,
                                         @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        listaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/produtos")
    public ResponseEntity<?> getProdutosInLista(@PathVariable String id,
                                                @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));

        // Busca todos os produtos cujos IDs estejam em produtosIds
        List<Produto> produtos = produtoRepository.findAllById(lista.getProdutosIds());
        return ResponseEntity.ok(produtos);
    }


    @PostMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> addProdutoToLista(@PathVariable String id,
                                               @PathVariable String id_produto,
                                               @RequestParam int quantidade,
                                               @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));

        lista.getProdutosIds().add(id_produto);
        lista.getProdutosQuantidades().add(quantidade);

        Lista listaAtualizada = listaRepository.save(lista);
        return ResponseEntity.ok(listaAtualizada);
    }

    @DeleteMapping("/{id}/produtos/{id_produto}")
    public ResponseEntity<?> removeProdutoFromLista(@PathVariable String id,
                                                    @PathVariable String id_produto,
                                                    @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido ou ausente!");
        }

        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));

        int index = lista.getProdutosIds().indexOf(id_produto);
        if (index != -1) {
            lista.getProdutosIds().remove(index);
            lista.getProdutosQuantidades().remove(index);
        }

        Lista listaAtualizada = listaRepository.save(lista);
        return ResponseEntity.ok(listaAtualizada);
    }

    @PutMapping("/{id}/produtos/{produtoId}")
    public ResponseEntity<?> updateProdutoQuantidade(@RequestHeader(value = "Authorization") String token,
                                                     @PathVariable String id,
                                                     @PathVariable String produtoId,
                                                     @RequestParam int quantidade) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }

        Lista lista = listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada"));

        int index = lista.getProdutosIds().indexOf(produtoId);
        if (index == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado na lista!");
        }

        if (quantidade <= 0) {
            lista.getProdutosIds().remove(index);
            lista.getProdutosQuantidades().remove(index);
        } else {
            lista.getProdutosQuantidades().set(index, quantidade);
        }

        Lista listaAtualizada = listaRepository.save(lista);

        return ResponseEntity.ok(listaAtualizada);
    }

}
