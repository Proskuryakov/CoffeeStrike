package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.FileNotLoadedException;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.NotFoundException;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SequenceService sequenceService;
    private final FileService fileService;

    public CategoryItem create(CategoryItem categoryItem, MultipartFile file) throws FileNotLoadedException {
        if (file == null) throw new FileNotLoadedException("Отсутствует файл");
        String id = sequenceService.getNextCategoryId();
        String imageLink = fileService.uploadFile(file, "category_" + id);
        return categoryRepository.insert(categoryItem.withCategoryid(id).withImageLink(imageLink)
        );
    }

    public List<CategoryItem> getAll() {
        return categoryRepository.findAll();
    }

    public CategoryItem getById(String categoryId) throws NotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public CategoryItem updateById(String categoryId, CategoryItem categoryItem, MultipartFile file) throws NotFoundException, FileNotLoadedException {
        String imageLink = file == null ? getById(categoryId).getImageLink() : fileService.uploadFile(file, "category_" + categoryId);
        return categoryRepository.save(categoryItem.withCategoryid(categoryId).withImageLink(imageLink));
    }

    public void deleteById(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryItem> search(String name) {
        return categoryRepository.findCategoryItemsByNameContains(name.toLowerCase());
    }
}
