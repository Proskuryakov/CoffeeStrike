package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryItem, String> {

    List<CategoryItem> findCategoryItemsByNameContains(String name);

}
