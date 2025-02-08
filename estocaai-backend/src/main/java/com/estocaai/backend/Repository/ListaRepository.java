package com.estocaai.backend.Repository;

import com.estocaai.backend.Classes.Lista;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaRepository extends MongoRepository<Lista, String> {
    List<Lista> findByUsuarioId(String usuarioId);

    List<Lista> findAllByUsuarioId(String usuarioId);
}
