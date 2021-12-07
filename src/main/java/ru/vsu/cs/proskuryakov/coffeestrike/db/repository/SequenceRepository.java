package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository {

    Long next(String sequence);

}
