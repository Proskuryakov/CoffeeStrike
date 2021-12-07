package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.CategoryAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Category;
import ru.vsu.cs.proskuryakov.coffeestrike.app.services.CategoryService;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryAPI {

    private final CategoryService categoryService;
    private final MapperFacade mapper;

    @Override
    public ResponseEntity<Category> create(Category params, MultipartFile file) {
        return Optional.of(params)
                .map(c -> mapper.map(c, CategoryItem.class))
                .map(c -> categoryService.create(c, file))
                .map(c -> mapper.map(c, Category.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<List<Category>> getAll() {
        return Optional.of(categoryService.getAll())
                .map(cl -> mapper.mapAsList(cl, Category.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Category> getById(String categoryId) {
        return Optional.of(categoryService.getById(categoryId))
                .map(c -> mapper.map(c, Category.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Category> update(String categoryId, Category params, MultipartFile file) {
        return Optional.of(params)
                .map(c -> mapper.map(c, CategoryItem.class))
                .map(c -> categoryService.updateById(categoryId, c, file))
                .map(c -> mapper.map(c, Category.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<?> delete(String categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Category>> search(String name) {
        return Optional.of(categoryService.search(name))
                .map(cl -> mapper.mapAsList(cl, Category.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
