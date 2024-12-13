package com.estocaai.backend.Repository;

import com.estocaai.backend.Classes.Dispensa;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DispensaRepository extends MongoRepository<Dispensa, String> {

}
