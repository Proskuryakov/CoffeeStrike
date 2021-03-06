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
        categoryItem1 = new CategoryItem().withCategoryid("1").withName("??????????").withImageLink(imageLink);
        categoryItem2 = new CategoryItem().withCategoryid("2").withName("??????").withImageLink(imageLink);
        drinkItem1 = new DrinkItem()
                .withDrinkid("1")
                .withName("???????????????????????? ??????????")
                .withCategoryItem(categoryItem1)
                .withImageLink(imageLink)
                .withDescription("????????????????")
                .withRecipe("????????????")
                .withVolumes(List.of("300", "400"))
                .withIngredients(List.of(
                        new Ingredient("????????????", "????", Map.of("300", 100d, "400", 150d)),
                        new Ingredient("????????????????", "????", Map.of("300", 20d, "400", 40d))
                ))
                .withCookingTime(5);
        drinkItem2 = new DrinkItem()
                .withDrinkid("2")
                .withName("???????????????????????? ??????")
                .withCategoryItem(categoryItem2)
                .withImageLink(imageLink)
                .withDescription("????????????????")
                .withRecipe("????????????")
                .withVolumes(List.of("300", "400"))
                .withIngredients(List.of(
                        new Ingredient("????????????", "????", Map.of("300", 100d, "400", 150d)),
                        new Ingredient("????????????????", "????", Map.of("300", 20d, "400", 40d)),
                        new Ingredient("?????????????????? ??????????", "????", Map.of("300", 10d, "400", 20d))
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
        var category = categoryItem1.withName("????????????????");
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
    @DisplayName("Search category by name - one result")
    void searchCategoryByNameSearchCategory1Test() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);

        var res = categoryRepository.findCategoryItemsByNameContains("??????");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(categoryItem1), res);
    }

    @Test
    @DisplayName("Search category by name - one result")
    void searchCategoryByNameSearchCategory2Test() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);

        var res = categoryRepository.findCategoryItemsByNameContains("??????");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(categoryItem2), res);
    }

    @Test
    @DisplayName("Search category by name - all test")
    void searchCategoryByNameAllCategoryTest() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);

        var res = categoryRepository.findCategoryItemsByNameContains("??");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(categoryItem1, categoryItem2), res);
    }

    @Test
    @DisplayName("Search category by name - empty result test")
    void searchCategoryByNameEmptyResultTest() {
        categoryRepository.insert(categoryItem1);
        categoryRepository.insert(categoryItem2);

        var res = categoryRepository.findCategoryItemsByNameContains("??");
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
    @DisplayName("Search drink by name in category 1 test")
    void searchDrinksByNameInCategory1Test() {
        categoryRepository.insert(categoryItem1);
        var drink2 = drinkItem1.withDrinkid("3").withName("?????????? ??????????????????");
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drink2);

        var res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "??????????");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(drinkItem1, drink2), res);
    }

    @Test
    @DisplayName("Search drink by name in category 2 test")
    void searchDrinksByNameInCategory2Test() {
        categoryRepository.insert(categoryItem1);
        var drink2 = drinkItem1.withDrinkid("3").withName("?????????? ??????????????????");
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drink2);

        var res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "??????????????????");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(drink2), res);
    }

    @Test
    @DisplayName("Search drink by name in category 3 test")
    void searchDrinksByNameInCategory3Test() {
        categoryRepository.insert(categoryItem1);
        var drink2 = drinkItem1.withDrinkid("3").withName("?????????? ??????????????????");
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drink2);

        var res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "??????????");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(drinkItem1), res);
    }

    @Test
    @DisplayName("Search drink by name in category 4 test")
    void searchDrinksByNameInCategory4Test() {
        categoryRepository.insert(categoryItem1);
        var drink2 = drinkItem1.withDrinkid("3").withName("?????????? ??????????????????");
        drinkRepository.insert(drinkItem1);
        drinkRepository.insert(drink2);

        var res = drinkRepository.findAllByCategoryItemIsAndNameContains(categoryItem1, "????????????????");
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
        var drink = drinkItem1.withName("??????????");
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
        var product = new ProductItem("1", "????????????");
        var res = productRepository.save(product);

        assertNotNull(res);
        assertEquals(product, res);
    }

    @Test
    @DisplayName("Get all product test")
    void getProductsTest() {
        var product1 = new ProductItem("1", "????????????");
        var product2 = new ProductItem("2", "????????????????");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAll();

        assertEquals(2, res.size());
        assertIterableEquals(List.of(product1, product2), res);
    }

    @Test
    @DisplayName("Search product by name test - first product test")
    void searchProductByNameOneResult1Test() {
        var product1 = new ProductItem("1", "????????????");
        var product2 = new ProductItem("2", "????????????????");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAllByNameContains("??????");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(product1), res);
    }

    @Test
    @DisplayName("Search product by name - second product test")
    void searchProductByNameOneResult2Test() {
        var product1 = new ProductItem("1", "????????????");
        var product2 = new ProductItem("2", "????????????????");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAllByNameContains("????");
        assertEquals(1, res.size());
        assertIterableEquals(List.of(product2), res);
    }

    @Test
    @DisplayName("Search product by name - all match test")
    void searchProductByNameAllMatchTest() {
        var product1 = new ProductItem("1", "????????????");
        var product2 = new ProductItem("2", "????????????????");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAllByNameContains("??");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(product1, product2), res);
    }

    @Test
    @DisplayName("Search product by name - empty result test")
    void searchProductByNameEmptyResultTest() {
        var product1 = new ProductItem("1", "????????????");
        var product2 = new ProductItem("2", "????????????????");

        productRepository.insert(product1);
        productRepository.insert(product2);

        var res = productRepository.findAllByNameContains("??");
        assertTrue(res.isEmpty());
    }

    @Test
    @DisplayName("Get all units test")
    void getUnitsTest() {
        var unit1 = new UnitItem("1", "????");
        var unit2 = new UnitItem("2", "????");
        var unit3 = new UnitItem("3", "????");
        unitRepository.insert(unit1);
        unitRepository.insert(unit2);
        unitRepository.insert(unit3);

        var res = unitRepository.findAll();
        assertEquals(3, res.size());
        assertIterableEquals(List.of(unit1, unit2, unit3), res);
    }

}