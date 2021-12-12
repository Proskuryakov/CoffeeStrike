package ru.vsu.cs.proskuryakov.coffeestrike.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Category;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Drink;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        value = "/api/drink",
        produces = {APPLICATION_JSON_VALUE}
)
public interface DrinkAPI {

    @PostMapping()
    ResponseEntity<Drink> create(
            @RequestPart(value = "params") Drink params,
            @RequestPart(value = "file") MultipartFile file
    );

    @GetMapping()
    ResponseEntity<List<Drink>> getAll();

    @GetMapping("/category/{categoryId}")
    ResponseEntity<List<Drink>> getAllByCategory(@PathVariable("categoryId") String categoryId);

    @GetMapping("/category/{categoryId}/search")
    ResponseEntity<List<Drink>> search(
            @PathVariable("categoryId") String categoryId,
            @RequestParam("name") String name
    );

    @GetMapping("/{drinkId}")
    ResponseEntity<Drink> getById(@PathVariable("drinkId") String drinkId);

    @PutMapping("/{drinkId}")
    ResponseEntity<Drink> update(
            @PathVariable("drinkId") String drinkId,
            @RequestPart(value = "params") Drink params,
            @RequestPart(value = "file", required = false) MultipartFile file
    );

    @DeleteMapping("/{drinkId}")
    ResponseEntity<?> delete(@PathVariable("drinkId") String drinkId);

}
