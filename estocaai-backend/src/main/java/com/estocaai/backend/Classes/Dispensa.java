package com.estocaai.backend.Classes;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "despensas")
public class Dispensa {
    @Id
    private String id;
    private String nome;

    private List<String> produtosIds = new ArrayList<>();

    private List<Integer> produtosQuantidades = new ArrayList<>();

    public Dispensa() {}

    public Dispensa(String nome) {
        this.nome = nome;
    }

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

    public List<Integer> getProdutosQuantidades() {
        return produtosQuantidades;
    }

    public void setProdutosQuantidades(List<Integer> produtosQuantidades) {
        this.produtosQuantidades = produtosQuantidades;
    }
}
