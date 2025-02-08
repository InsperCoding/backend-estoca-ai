package com.estocaai.backend.Controller;

import com.estocaai.backend.Repository.ProdutoRepository;
import com.estocaai.backend.Classes.Dispensa;
import com.estocaai.backend.Classes.Produto;
// import com.estocaai.backend.Repository.DispensaRepository;
import com.estocaai.backend.Repository.CasaRepository;
import com.estocaai.backend.Classes.Casa;
import com.estocaai.backend.User.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/casas/{id_casa}/dispensas")
public class DispensaController {

    // @Autowired
    // private DispensaRepository dispensaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CasaRepository casaRepository;

    @Autowired
    private UserService userService;

    // Lista todas as dispensas de uma casa específica
    @GetMapping
    public ResponseEntity<?> getAllDispensas(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable("id_casa") String casaId) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));
        List<Dispensa> dispensas = casa.getDispensas();

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        return ResponseEntity.ok(dispensas);
    }

    // Cria uma nova dispensa em uma casa específica
    @PostMapping
    public ResponseEntity<?> createDispensa(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable("id_casa") String casaId,
                                            @RequestBody Dispensa dispensa) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));
        casa.getDispensas().add(dispensa);
        casaRepository.save(casa);

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        return ResponseEntity.status(HttpStatus.CREATED).body(dispensa);
    }

    // Retorna os detalhes de uma dispensa específica
    @GetMapping("/{dispensaId}")
    public ResponseEntity<?> getDispensaById(@PathVariable("id_casa") String casaId,
                                             @PathVariable String dispensaId,
                                             @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        Dispensa dispensa = casa.getDispensas().stream()
                .filter(d -> d.getId().equals(dispensaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        return ResponseEntity.ok(dispensa);
    }

    // Remove uma dispensa de uma casa específica
    @DeleteMapping("/{dispensaId}")
    public ResponseEntity<?> deleteDispensa(@PathVariable("id_casa") String casaId,
                                            @PathVariable String dispensaId,
                                            @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);
        
        casa.getDispensas().removeIf(d -> d.getId().equals(dispensaId));
        casaRepository.save(casa);
        return ResponseEntity.noContent().build();
    }

    // Retorna os produtos de uma dispensa específica
    @GetMapping("/{dispensaId}/produtos")
    public ResponseEntity<?> getProdutosInDispensa(@PathVariable("id_casa") String casaId,
                                                   @PathVariable String dispensaId,
                                                   @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        Dispensa dispensa = casa.getDispensas().stream()
                .filter(d -> d.getId().equals(dispensaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        List<Produto> produtos = produtoRepository.findAllById(dispensa.getProdutosIds());
        return ResponseEntity.ok(produtos);
    }

    // Adiciona um produto a uma dispensa específica
    @PostMapping("/{dispensaId}/produtos/{id_produto}")
    public ResponseEntity<?> addProdutoToDispensa(@PathVariable("id_casa") String casaId,
                                                  @PathVariable String dispensaId,
                                                  @PathVariable String id_produto,
                                                  @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        Dispensa dispensa = casa.getDispensas().stream()
                .filter(d -> d.getId().equals(dispensaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        dispensa.getProdutosIds().add(id_produto);
        casaRepository.save(casa);
        return ResponseEntity.ok(dispensa);
    }

    // Remove um produto de uma dispensa específica
    @DeleteMapping("/{dispensaId}/produtos/{id_produto}")
    public ResponseEntity<?> removeProdutoFromDispensa(@PathVariable("id_casa") String casaId,
                                                       @PathVariable String dispensaId,
                                                       @PathVariable String id_produto,
                                                       @RequestHeader(value = "Authorization") String token) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        Dispensa dispensa = casa.getDispensas().stream()
                .filter(d -> d.getId().equals(dispensaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        dispensa.getProdutosIds().remove(id_produto);
        casaRepository.save(casa);
        return ResponseEntity.ok(dispensa);
    }

    @PutMapping("/{dispensaId}/produtos/{id_produto}")
    public ResponseEntity<?> updateProdutoQuantidade(@RequestHeader(value = "Authorization") String token,
                                                     @PathVariable("id_casa") String casaId,
                                                     @PathVariable String dispensaId,
                                                     @PathVariable String id_produto,
                                                     @RequestParam int quantidade) {
        if (!userService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou ausente!");
        }
        Casa casa = casaRepository.findById(casaId)
                .orElseThrow(() -> new RuntimeException("Casa não encontrada"));

        String userId = userService.getUsuarioIdFromToken(token);
        userService.escolherCasa(userId, casaId);

        Dispensa dispensa = casa.getDispensas().stream()
                .filter(d -> d.getId().equals(dispensaId))
                .findFirst()
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

        casaRepository.save(casa);

        return ResponseEntity.ok(dispensa);
    }

}
