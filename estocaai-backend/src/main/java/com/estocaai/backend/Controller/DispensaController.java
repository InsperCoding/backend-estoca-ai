package com.estocaai.backend.Controller;

import com.estocaai.backend.Repository.ProdutoRepository;
import com.estocaai.backend.Classes.Dispensa;
import com.estocaai.backend.Classes.Produto;
import com.estocaai.backend.Repository.DispensaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dispensas")
public class DispensaController {

    @Autowired
    private DispensaRepository dispensaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Lista todas as dispensas
    @GetMapping
    public List<Dispensa> getAllDispensas() {
        return dispensaRepository.findAll();
    }

    // Cria uma nova dispensa
    @PostMapping
    public Dispensa createDispensa(@RequestBody Dispensa dispensa) {
        return dispensaRepository.save(dispensa);
    }

    // Retorna os detalhes de uma dispensa específica
    @GetMapping("/{id}")
    public Dispensa getDispensaById(@PathVariable String id) {
        return dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
    }

    // Remove uma dispensa
    @DeleteMapping("/{id}")
    public void deleteDispensa(@PathVariable String id) {
        dispensaRepository.deleteById(id);
    }

    // Retorna os produtos de uma dispensa específica
    @GetMapping("/{id}/produtos")
    public List<Produto> getProdutosInDispensa(@PathVariable String id) {
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        return produtoRepository.findAllById(dispensa.getProdutosIds());
    }

    // Adiciona um produto a uma dispensa
    @PostMapping("/{id}/produtos/{id_produto}")
    public Dispensa addProdutoToDispensa(@PathVariable String id, @PathVariable String id_produto) {
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        dispensa.getProdutosIds().add(id_produto);
        return dispensaRepository.save(dispensa);
    }

    // Remove um produto de uma dispensa
    @DeleteMapping("/{id}/produtos/{id_produto}")
    public Dispensa removeProdutoFromDispensa(@PathVariable String id, @PathVariable String id_produto) {
        Dispensa dispensa = dispensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispensa não encontrada"));
        dispensa.getProdutosIds().remove(id_produto);
        return dispensaRepository.save(dispensa);
    }
}
