package ru.vsu.cs.proskuryakov.coffeestrike.db.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.Sequence;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.SequenceRepository;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class SequenceRepositoryImpl implements SequenceRepository {

	private final MongoOperations mongoOperations;

	public Long next(String seqName) {
		var sequence = mongoOperations.findAndModify(
			Query.query(Criteria.where("_id").is(seqName)),
			new Update().inc("value", 1),
			FindAndModifyOptions.options().returnNew(true).upsert(true),
			Sequence.class
		);
		return Objects.isNull(sequence) ? 1 : sequence.getValue();
	}

}
