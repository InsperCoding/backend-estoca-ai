package com.estocaai.backend.Repository;

import com.estocaai.backend.Classes.Lista;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaRepository extends MongoRepository<Lista, String> {
    
}
