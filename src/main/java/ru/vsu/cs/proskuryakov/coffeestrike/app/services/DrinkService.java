package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.FileNotLoadedException;
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
    private final FileService fileService;

    @SneakyThrows
    public DrinkItem create(DrinkItem drinkItem, MultipartFile file) {
        if (file == null) throw new FileNotLoadedException("Отсутствует файл");
        String id = sequenceService.getNextDrinkId();
        String imageLink = fileService.uploadFile(file, "drink_" + id);
        return drinkRepository.insert(drinkItem
                .withDrinkid(id)
                .withImageLink(imageLink)
        );
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

    @SneakyThrows
    public DrinkItem getById(String drinkId) {
        return drinkRepository.findById(drinkId).orElseThrow(() -> new NotFoundException("Drink not found"));
    }
    @SneakyThrows
    public DrinkItem updateById(String drinkId, DrinkItem drinkItem, MultipartFile file) {
        String imageLink = file == null ? getById(drinkId).getImageLink() : fileService.uploadFile(file, "drink_" + drinkId);
        return drinkRepository.save(drinkItem.withDrinkid(drinkId).withImageLink(imageLink));
    }

    public void deleteById(String drinkId) {
        drinkRepository.deleteById(drinkId);
    }
}
