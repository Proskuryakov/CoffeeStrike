package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.NotFoundException;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.DrinkItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.DrinkRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final SequenceService sequenceService;

    public DrinkItem create(DrinkItem drinkItem) {
        return drinkRepository.insert(drinkItem.withDrinkid(sequenceService.getNextDrinkId()));
    }

    public List<DrinkItem> getAll() {
        return drinkRepository.findAll();
    }

    public List<DrinkItem> getAllByCategory(CategoryItem categoryItem) {
        return drinkRepository.findAllByCategoryItemIs(categoryItem);
    }

    public List<DrinkItem> search(CategoryItem categoryItem, String name) {
        return drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem, name.toLowerCase());
    }

    public DrinkItem getById(String drinkId) throws NotFoundException {
        return drinkRepository.findById(drinkId).orElseThrow(() -> new NotFoundException("Drink not found"));
    }

    public DrinkItem updateById(String drinkId, DrinkItem drinkItem) {
        return drinkRepository.save(drinkItem.withDrinkid(drinkId));
    }

    public void deleteById(String drinkId) {
        drinkRepository.deleteById(drinkId);
    }
}
