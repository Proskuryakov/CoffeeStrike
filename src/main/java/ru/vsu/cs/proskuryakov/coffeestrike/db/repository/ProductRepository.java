package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.ProductItem;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductItem, String> {

    List<ProductItem> findAllByNameContains(String name);

}
