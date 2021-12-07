package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import org.springframework.http.ResponseEntity;
import ru.vsu.cs.proskuryakov.coffeestrike.api.ProductAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Product;

import java.util.List;

public class ProductController implements ProductAPI {
    @Override
    public ResponseEntity<Product> create(Product params) {
        return null;
    }

    @Override
    public ResponseEntity<List<Product>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<List<Product>> search(String name) {
        return null;
    }
}
