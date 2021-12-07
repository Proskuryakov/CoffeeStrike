package ru.vsu.cs.proskuryakov.coffeestrike.db.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Ingredient;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.DrinkItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.ProductItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.UnitItem;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class RepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UnitRepository unitRepository;

    private static CategoryItem categoryItem1;
    private static CategoryItem categoryItem2;
    private static DrinkItem drinkItem1;
    private static DrinkItem drinkItem2;

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

    @AfterEach
    private void clearRepositories() {
        categoryRepository.deleteAll();
        drinkRepository.deleteAll();
        productRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    @DisplayName("Create category test")
    void createCategoryTest() {
        var category = categoryRepository.insert(categoryItem1);
        assertNotNull(category);
        assertEquals(categoryItem1, category);
    }

    @Test
    @DisplayName("Get all category test")
    void getCategoriesTest() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);

        var categories = categoryRepository.findAll();

        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertIterableEquals(List.of(categoryItem1, categoryItem2), categories);
    }

    @Test
    @DisplayName("Get category by id test")
    void getCategoryByIdTest() {
        categoryRepository.insert(categoryItem1);
        var c = categoryRepository.findById("1");
        assertNotNull(c);
        assertTrue(c.isPresent());
        assertEquals(categoryItem1, c.get());
    }

    @Test
    @DisplayName("Update category by id test")
    void updateCategoryByIdTest() {
        categoryRepository.insert(categoryItem1);
        var category = categoryItem1.withName("Капучино");
        var newCategory = categoryRepository.save(category);

        assertNotNull(newCategory);
        assertEquals(category, newCategory);
    }

    @Test
    @DisplayName("Delete category by id test")
    void deleteCategoryByIdTest() {
        categoryRepository.insert(categoryItem1);

        var c = categoryRepository.findById("1");
        assertNotNull(c);
        assertTrue(c.isPresent());

        categoryRepository.deleteById("1");

        c = categoryRepository.findById("1");
        assertNotNull(c);
        assertFalse(c.isPresent());
    }

    @Test
    @DisplayName("Search category by name test")
    void searchCategoryByNameTest() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);

        var res = categoryRepository.findCategoryItemsByNameContains("лат");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(categoryItem1), res);

        res = categoryRepository.findCategoryItemsByNameContains("раф");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(categoryItem2), res);

        res = categoryRepository.findCategoryItemsByNameContains("а");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(categoryItem1, categoryItem2), res);

        res = categoryRepository.findCategoryItemsByNameContains("б");
        assertTrue(res.isEmpty());
    }

    @Test
    @DisplayName("Create drink test")
    void createDrinkTest() {
        var drink = drinkRepository.insert(drinkItem1);
        assertNotNull(drink);
        assertEquals(drinkItem1, drink);
    }

    @Test
    @DisplayName("Get all drink test")
    void getDrinksTest() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drinkItem2);

        var drinks = drinkRepository.findAll();

        assertEquals(2, drinks.size());
        assertIterableEquals(List.of(drinkItem1, drinkItem2), drinks);
    }

    @Test
    @DisplayName("Get drinks in category test")
    void getDrinksByCategoryTest() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drinkItem2);

        var drinks = drinkRepository.findAllByCategoryItemIs(categoryItem1);
        assertEquals(1, drinks.size());
        assertIterableEquals(List.of(drinkItem1), drinks);

        drinks = drinkRepository.findAllByCategoryItemIs(categoryItem2);
        assertEquals(1, drinks.size());
        assertIterableEquals(List.of(drinkItem2), drinks);
    }

    @Test
    @DisplayName("Search drink by name in category test")
    void searchDrinksByNameInCategoryTest() {
        categoryRepository.insert(categoryItem1);
        var drink2 = drinkItem1.withDrinkid("3").withName("латте смородина");
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drink2);

        var res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "латте");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(drinkItem1, drink2), res);

        res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "смородина");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(drink2), res);

        res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "класс");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(drinkItem1), res);

        res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "карамель");
        assertTrue(res.isEmpty());
    }

    @Test
    @DisplayName("Get drink by id test")
    void getDrinkByIdTest() {
        categoryRepository.insert(categoryItem1);
        drinkRepository.insert(drinkItem1);

        var res = drinkRepository.findById("1");

        assertTrue(res.isPresent());
        assertEquals(drinkItem1, res.get());
    }

    @Test
    @DisplayName("Update drink by id test")
    void updateDrinkByIdTest() {
        categoryRepository.insert(categoryItem1);
        drinkRepository.insert(drinkItem1);
        var drink = drinkItem1.withName("новый");
        var newDrink = drinkRepository.save(drink);

        assertNotNull(newDrink);
        assertEquals(drink, newDrink);
    }

    @Test
    @DisplayName("Delete drink by id test")
    void deleteDrinkByIdTest() {
        drinkRepository.insert(drinkItem1);

        var c = drinkRepository.findById("1");
        assertNotNull(c);
        assertTrue(c.isPresent());

        drinkRepository.deleteById("1");

        c = drinkRepository.findById("1");
        assertNotNull(c);
        assertFalse(c.isPresent());
    }

    @Test
    @DisplayName("Create product test")
    void createProductTest() {
        var product = new ProductItem("1", "молоко");
        var res = productRepository.save(product);

        assertNotNull(res);
        assertEquals(product, res);
    }

    @Test
    @DisplayName("Get all product test")
    void getProductsTest() {
        var product1 = new ProductItem("1", "молоко");
        var product2 = new ProductItem("2", "эспрессо");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAll();

        assertEquals(2, res.size());
        assertIterableEquals(List.of(product1, product2), res);
    }

    @Test
    @DisplayName("Search product by name test")
    void searchProductByNameTest() {
        var product1 = new ProductItem("1", "молоко");
        var product2 = new ProductItem("2", "эспрессо");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAllByNameContains("мол");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(product1), res);

        res = productRepository.findAllByNameContains("эс");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(product2), res);

        res = productRepository.findAllByNameContains("о");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(product1, product2), res);

        res = productRepository.findAllByNameContains("ф");
        assertTrue(res.isEmpty());
    }

    @Test
    @DisplayName("Get all units test")
    void getUnitsTest() {
        var unit1 = new UnitItem("1", "мл");
        var unit2 = new UnitItem("2", "гр");
        var unit3 = new UnitItem("3", "шт");
        unitRepository.insert(unit1);
        unitRepository.insert(unit2);
        unitRepository.insert(unit3);

        var res = unitRepository.findAll();
        assertEquals(3, res.size());
        assertIterableEquals(List.of(unit1, unit2, unit3), res);
    }

}