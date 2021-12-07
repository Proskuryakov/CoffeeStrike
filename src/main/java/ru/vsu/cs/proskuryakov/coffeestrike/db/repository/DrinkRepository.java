package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.DrinkItem;

import java.util.List;

@Repository
public interface DrinkRepository extends MongoRepository<DrinkItem, String> {

    List<DrinkItem> findAllByCategoryItemIs(CategoryItem categoryItem);

    List<DrinkItem> findAllByCategoryItemIsAndNameContains(CategoryItem categoryItem, String name);

}
