package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.DrinkRepository;

@Service
@AllArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final SequenceService sequenceService;

}
