package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Ingredient;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.FileNotLoadedException;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.NotFoundException;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.DrinkItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.DrinkRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrinkServiceTest {

    @Mock
    private static DrinkRepository drinkRepository;
    @Mock
    private static SequenceService sequenceService;
    @Mock
    private static FileService fileService;
    @InjectMocks
    private DrinkService drinkService;

    private static CategoryItem categoryItem1;
    private static CategoryItem categoryItem2;
    private static DrinkItem drinkItem1;
    private static DrinkItem drinkItem2;
    private final MultipartFile file = new MockMultipartFile("file", "file".getBytes());

    @BeforeAll
    private static void initModels() {
        String imageLink = "https://image.coffeestrike.ru";
        categoryItem1 = new CategoryItem().withCategoryid("1").withName("латте").withImageLink(imageLink);
        categoryItem2 = new CategoryItem().withCategoryid("2").withName("раф").withImageLink(imageLink);
        drinkItem1 = new DrinkItem()
                .withDrinkid("1")
                .withName("классический латте")
                .withCategoryItem(categoryItem1)
                .withImageLink(imageLink)
                .withDescription("Описание")
                .withRecipe("Рецепт")
                .withVolumes(List.of("300", "400"))
                .withIngredients(List.of(
                        new Ingredient("молоко", "мл", Map.of("300", 100d, "400", 150d)),
                        new Ingredient("эспрессо", "мл", Map.of("300", 20d, "400", 40d))
                ))
                .withCookingTime(5);
        drinkItem2 = new DrinkItem()
                .withDrinkid("2")
                .withName("классический раф")
                .withCategoryItem(categoryItem2)
                .withImageLink(imageLink)
                .withDescription("Описание")
                .withRecipe("Рецепт")
                .withVolumes(List.of("300", "400"))
                .withIngredients(List.of(
                        new Ingredient("молоко", "мл", Map.of("300", 100d, "400", 150d)),
                        new Ingredient("эспрессо", "мл", Map.of("300", 20d, "400", 40d)),
                        new Ingredient("ванильный сахар", "гр", Map.of("300", 10d, "400", 20d))
                ))
                .withCookingTime(5);
    }

    @BeforeEach
    private void init() {
        drinkService = new DrinkService(drinkRepository, sequenceService, fileService);
    }

    @Test
    @DisplayName("Create drink test")
    void createDrinkTest() throws FileNotLoadedException {
        when(sequenceService.getNextDrinkId()).thenReturn("1");
        when(drinkRepository.insert(any(DrinkItem.class))).then(returnsFirstArg());
        when(fileService.uploadFile(any(MultipartFile.class), any(String.class))).thenReturn("https://image.coffeestrike.ru");

        var drink = drinkService.create(drinkItem1, file);
        assertNotNull(drink);
        assertEquals(drinkItem1, drink);
    }

    @Test
    @DisplayName("Create drink without file test")
    void createDrinkFailTest() {
        assertThrows(FileNotLoadedException.class, () -> drinkService.create(drinkItem1, null));
    }

    @Test
    @DisplayName("Get all drink test")
    void getDrinksTest() {
        when(drinkRepository.findAll()).thenReturn(List.of(drinkItem1, drinkItem2));

        var drinks = drinkService.getAll();

        assertEquals(2, drinks.size());
        assertIterableEquals(List.of(drinkItem1, drinkItem2), drinks);
    }

    @Test
    @DisplayName("Get drinks in category test")
    void getDrinksByCategoryTest() {
        when(drinkRepository.findAllByCategoryItemIs(categoryItem1)).thenReturn(List.of(drinkItem1));

        var drinks = drinkService.getAllByCategory(categoryItem1);
        assertEquals(1, drinks.size());
        assertIterableEquals(List.of(drinkItem1), drinks);
    }

    @Test
    @DisplayName("Search drink by name in category test")
    void searchDrinksByNameInCategoryTest() {
        var drink2 = drinkItem1.withDrinkid("3").withName("латте смородина");
        when(drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "латте"))
                .thenReturn(List.of(drinkItem1, drink2));

        var res = drinkService.search(categoryItem1, "латте");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(drinkItem1, drink2), res);
    }

    @Test
    @DisplayName("Get drink by id test")
    void getDrinkByIdTest() throws NotFoundException {
        when(drinkRepository.findById(any(String.class))).thenReturn(Optional.of(drinkItem1));

        var res = drinkService.getById("1");

        assertNotNull(res);
        assertEquals(drinkItem1, res);
    }

    @Test
    @DisplayName("Try get drink by id and catch exception test")
    void getCategoryByIdAndCatchExceptionTest() {
        when(drinkRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> drinkService.getById("100"));
    }

    @Test
    @DisplayName("Update drink by id test")
    void updateDrinkByIdTest() throws NotFoundException, FileNotLoadedException {
        when(drinkRepository.save(any(DrinkItem.class))).then(returnsFirstArg());
        when(fileService.uploadFile(any(MultipartFile.class), any(String.class))).thenReturn("https://image.coffeestrike.ru");

        var newDrink = drinkService.updateById("2", drinkItem1, file);
        assertNotNull(newDrink);
        assertEquals(drinkItem1.withDrinkid("2"), newDrink);
    }

    @Test
    @DisplayName("Update drink by id without file test")
    void updateDrinkByIdWithoutFileTest() throws NotFoundException, FileNotLoadedException {
        when(drinkRepository.save(any(DrinkItem.class))).then(returnsFirstArg());
        when(drinkRepository.findById(any(String.class))).thenReturn(Optional.of(drinkItem1));

        var newDrink = drinkService.updateById("2", drinkItem1, null);
        assertNotNull(newDrink);
        assertEquals(drinkItem1.withDrinkid("2"), newDrink);
    }

}