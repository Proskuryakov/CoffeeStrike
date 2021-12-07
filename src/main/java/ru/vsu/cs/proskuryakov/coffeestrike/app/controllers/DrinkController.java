package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.DrinkAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Category;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Drink;

import java.util.List;

public class DrinkController implements DrinkAPI {
    @Override
    public ResponseEntity<Drink> create(Drink params, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<List<Drink>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<List<Drink>> getAllByCategory(String categoryId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Category>> search(String categoryId, String name) {
        return null;
    }

    @Override
    public ResponseEntity<Drink> getById(String drinkId) {
        return null;
    }

    @Override
    public ResponseEntity<Drink> update(String drinkId, Drink params, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(String drinkId) {
        return null;
    }
}
