package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.UnitItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.UnitRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    public List<UnitItem> getAll() {
        return unitRepository.findAll();
    }
}
