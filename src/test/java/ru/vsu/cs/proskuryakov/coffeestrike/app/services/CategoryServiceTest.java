package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    private static CategoryService categoryService;

    private static CategoryItem categoryItem1;
    private static CategoryItem categoryItem2;

    @BeforeAll
    private static void init() {
        categoryService = new CategoryService(categoryRepository, sequenceService);

        when(sequenceService.getNextCategoryId()).thenReturn("1");

        String imageLink = "https://image.coffeestrike.ru";
        categoryItem1 = new CategoryItem().withCategoryid("1").withName("латте").withImageLink(imageLink);
        categoryItem2 = new CategoryItem().withCategoryid("2").withName("раф").withImageLink(imageLink);
    }

    @Test
    @DisplayName("Create category test")
    void createCategoryTest() {
        when(categoryRepository.insert(any(CategoryItem.class))).then(returnsFirstArg());

        var category = categoryService.create(categoryItem1);
        assertNotNull(category);
        assertEquals(categoryItem1, category);
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
    void getCategoryByIdTest() {
        when(categoryRepository.findById(any(String.class))).thenReturn(Optional.of(categoryItem1));
        var c = categoryService.getById("1");
        assertNotNull(c);
        assertEquals(categoryItem1, c);
    }

    @Test
    @DisplayName("Try get category by id and catch exception test")
    void getCategoryByIdAndCatchExceptionTest() {
        when(categoryRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, categoryService.getById("1"));
    }

    @Test
    @DisplayName("Update category by id test")
    void updateCategoryByIdTest() {
        when(categoryRepository.save(any(CategoryItem.class))).then(returnsFirstArg());

        var newCategory = categoryService.updateById("2", categoryItem1);
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