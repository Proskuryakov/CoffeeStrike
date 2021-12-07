package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.UnitItem;

@Repository
public interface UnitRepository extends MongoRepository<UnitItem, String> {
}
