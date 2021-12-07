package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.CategoryAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Category;

import java.util.List;

public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<Category> create(Category params, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<List<Category>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<Category> getById(String categoryId) {
        return null;
    }

    @Override
    public ResponseEntity<Category> update(String categoryId, Category params, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(String categoryId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Category>> search(String name) {
        return null;
    }
}
