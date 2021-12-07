package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.NotFoundException;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SequenceService sequenceService;

    public CategoryItem create(CategoryItem categoryItem) {
        return categoryRepository.insert(categoryItem.withCategoryid(sequenceService.getNextCategoryId()));
    }

    public List<CategoryItem> getAll() {
        return categoryRepository.findAll();
    }

    public CategoryItem getById(String categoryId) throws NotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public CategoryItem updateById(String categoryId, CategoryItem categoryItem) {
        return categoryRepository.save(categoryItem.withCategoryid(categoryId));
    }

    public void deleteById(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryItem> search(String name) {
        return categoryRepository.findCategoryItemsByNameContains(name.toLowerCase());
    }
}
