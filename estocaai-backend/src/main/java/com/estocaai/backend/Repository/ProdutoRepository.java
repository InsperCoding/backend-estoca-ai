package com.estocaai.backend.Repository;

import com.estocaai.backend.Classes.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
    
}
