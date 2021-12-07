package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.DrinkAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Drink;
import ru.vsu.cs.proskuryakov.coffeestrike.app.services.CategoryService;
import ru.vsu.cs.proskuryakov.coffeestrike.app.services.DrinkService;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.DrinkItem;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DrinkController implements DrinkAPI {

    private final DrinkService drinkService;
    private final CategoryService categoryService;
    private final MapperFacade mapper;

    @Override
    public ResponseEntity<Drink> create(Drink params, MultipartFile file) {
        return Optional.of(params)
                .map(d -> mapper.map(d, DrinkItem.class))
                .map(d -> drinkService.create(d, file))
                .map(d -> mapper.map(d, Drink.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<List<Drink>> getAll() {
        return Optional.of(drinkService.getAll())
                .map(dl -> mapper.mapAsList(dl, Drink.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<List<Drink>> getAllByCategory(String categoryId) {
        return Optional.of(categoryService.getById(categoryId))
                .map(drinkService::getAllByCategory)
                .map(d -> mapper.mapAsList(d, Drink.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<List<Drink>> search(String categoryId, String name) {
        return Optional.of(categoryService.getById(categoryId))
                .map(c -> drinkService.search(c, name))
                .map(d -> mapper.mapAsList(d, Drink.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Drink> getById(String drinkId) {
        return Optional.of(drinkService.getById(drinkId))
                .map(c -> mapper.map(c, Drink.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Drink> update(String drinkId, Drink params, MultipartFile file) {
        return Optional.of(params)
                .map(d -> mapper.map(d, DrinkItem.class))
                .map(d -> drinkService.updateById(drinkId, d, file))
                .map(d -> mapper.map(d, Drink.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<?> delete(String drinkId) {
        drinkService.deleteById(drinkId);
        return ResponseEntity.ok().build();
    }

}
