package com.estocaai.backend.Repository;

import com.estocaai.backend.Classes.Casa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepository extends MongoRepository<Casa, String> {
}
