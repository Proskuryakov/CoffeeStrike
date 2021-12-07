package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.SequenceRepository;

@Service
@AllArgsConstructor
public class SequenceService {

    private final SequenceRepository sequenceRepository;

    private final static String CATEGORY_SEQUENCE_NAME = "categoryid";
    private final static String DRINK_SEQUENCE_NAME = "drinkid";
    private final static String PRODUCT_SEQUENCE_NAME = "productid";

    public String getNextCategoryId() {
        return getNextId(CATEGORY_SEQUENCE_NAME);
    }

    public String getNextDrinkId() {
        return getNextId(DRINK_SEQUENCE_NAME);
    }

    public String getNextProductId() {
        return getNextId(PRODUCT_SEQUENCE_NAME);
    }

    private String getNextId(String sequenceName) {
        return sequenceRepository.next(sequenceName).toString();
    }
}
