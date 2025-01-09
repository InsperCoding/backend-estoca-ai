package com.estocaai.backend.Classes;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "listas")
public class Lista {
    @Id
    private String id;
    private String nome;
    private List<String> produtosIds = new ArrayList<>(); // Lista de IDs dos produtos

    // Construtores
    public Lista() {}

    public Lista(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getProdutosIds() {
        return produtosIds;
    }

    public void setProdutosIds(List<String> produtosIds) {
        this.produtosIds = produtosIds;
    }
}
