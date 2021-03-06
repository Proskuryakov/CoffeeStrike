package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.FileNotLoadedException;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.NotFoundException;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private static CategoryRepository categoryRepository;
    @Mock
    private static SequenceService sequenceService;
    @Mock
    private static FileService fileService;
    @InjectMocks
    private CategoryService categoryService;

    private final CategoryItem categoryItem1 = new CategoryItem()
            .withCategoryid("1").withName("латте").withImageLink("https://image.coffeestrike.ru");
    private final CategoryItem categoryItem2 = new CategoryItem()
            .withCategoryid("2").withName("раф").withImageLink("https://image.coffeestrike.ru");
    private final MultipartFile file = new MockMultipartFile("file", "file".getBytes());

    @BeforeEach
    private void init() {
        categoryService = new CategoryService(categoryRepository, sequenceService, fileService);
    }

    @Test
    @DisplayName("Create category test")
    void createCategoryTest() throws FileNotLoadedException {
        when(sequenceService.getNextCategoryId()).thenReturn("1");
        when(categoryRepository.insert(any(CategoryItem.class))).then(returnsFirstArg());
        when(fileService.uploadFile(any(MultipartFile.class), any(String.class))).thenReturn("https://image.coffeestrike.ru");

        var category = categoryService.create(categoryItem1, file);
        assertNotNull(category);
        assertEquals(categoryItem1, category);
    }

    @Test
    @DisplayName("Create category without file test")
    void createCategoryFailTest() {
        assertThrows(FileNotLoadedException.class, () -> categoryService.create(categoryItem1, null));
    }

    @Test
    @DisplayName("Get all category test")
    void getCategoriesTest() {
        when(categoryRepository.findAll()).thenReturn(List.of(categoryItem1, categoryItem2));

        var categories = categoryService.getAll();

        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertIterableEquals(List.of(categoryItem1, categoryItem2), categories);
    }

    @Test
    @DisplayName("Get category by id test")
    void getCategoryByIdTest() throws NotFoundException {
        when(categoryRepository.findById(any(String.class))).thenReturn(Optional.of(categoryItem1));
        var c = categoryService.getById("1");
        assertNotNull(c);
        assertEquals(categoryItem1, c);
    }

    @Test
    @DisplayName("Try get category by id and catch exception test")
    void getCategoryByIdAndCatchExceptionTest() {
        when(categoryRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> categoryService.getById("100"));
    }

    @Test
    @DisplayName("Update category by id test")
    void updateCategoryByIdTest() throws NotFoundException, FileNotLoadedException {
        when(categoryRepository.save(any(CategoryItem.class))).then(returnsFirstArg());
        when(fileService.uploadFile(any(MultipartFile.class), any(String.class))).thenReturn("https://image.coffeestrike.ru");

        var newCategory = categoryService.updateById("2", categoryItem1, file);
        assertNotNull(newCategory);
        assertEquals(categoryItem1.withCategoryid("2"), newCategory);
    }

    @Test
    @DisplayName("Update category by id without file test")
    void updateCategoryByIdWithoutFileTest() throws NotFoundException, FileNotLoadedException {
        when(categoryRepository.findById(any(String.class))).thenReturn(Optional.of(categoryItem1));
        when(categoryRepository.save(any(CategoryItem.class))).then(returnsFirstArg());

        var newCategory = categoryService.updateById("2", categoryItem1, null);
        assertNotNull(newCategory);
        assertEquals(categoryItem1.withCategoryid("2"), newCategory);
    }


    @Test
    @DisplayName("Search category by name test")
    void searchCategoryByNameTest() {
        when(categoryRepository.findCategoryItemsByNameContains(any(String.class)))
                .thenReturn(List.of(categoryItem1, categoryItem2));

        var res = categoryService.search("a");

        assertEquals(2, res.size());
        assertIterableEquals(List.of(categoryItem1, categoryItem2), res);
    }

}