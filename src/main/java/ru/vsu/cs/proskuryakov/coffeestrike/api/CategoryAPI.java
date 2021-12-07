package ru.vsu.cs.proskuryakov.coffeestrike.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Category;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        value = "/category",
        produces = {APPLICATION_JSON_VALUE}
)
public interface CategoryAPI {

    @PostMapping()
    ResponseEntity<Category> create(
            @RequestPart(value = "params") Category params,
            @RequestPart(value = "file") MultipartFile file
    );

    @GetMapping()
    ResponseEntity<List<Category>> getAll();

    @GetMapping("/{categoryId}")
    ResponseEntity<Category> getById(@PathVariable("categoryId") String categoryId);

    @PutMapping("/{categoryId}")
    ResponseEntity<Category> update(
            @PathVariable("categoryId") String categoryId,
            @RequestPart(value = "params") Category params,
            @RequestPart(value = "file", required = false) MultipartFile file
    );

    @DeleteMapping("/{categoryId}")
    ResponseEntity<?> delete(@PathVariable("categoryId") String categoryId);

    @GetMapping("/search")
    ResponseEntity<List<Category>> search(@RequestParam("name") String name);

}
