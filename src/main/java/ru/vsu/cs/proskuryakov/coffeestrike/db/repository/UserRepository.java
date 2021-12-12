package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.UserItem;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserItem, String> {

    Optional<UserItem> findUserItemByUsername(String username);

}
