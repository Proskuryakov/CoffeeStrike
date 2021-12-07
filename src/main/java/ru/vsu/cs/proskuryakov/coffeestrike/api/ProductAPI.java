package ru.vsu.cs.proskuryakov.coffeestrike.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Product;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        value = "/product",
        produces = {APPLICATION_JSON_VALUE}
)
public interface ProductAPI {

    @PostMapping()
    ResponseEntity<Product> create(@RequestBody Product params);

    @GetMapping()
    ResponseEntity<List<Product>> getAll();

    @GetMapping("/search")
    ResponseEntity<List<Product>> search(@RequestParam("name") String name);

}
