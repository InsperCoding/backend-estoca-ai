package com.estocaai.backend.Classes;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "produtos")
public class Produto {
    @Id
    private String id;
    private String nome;
    private String descricao;
    private String imagemb64;

    public String getImagemb64() {
        return imagemb64;
    }

    public void setImagemb64(String imagemb64) {
        this.imagemb64 = imagemb64;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
