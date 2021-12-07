package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.proskuryakov.coffeestrike.api.ProductAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Product;
import ru.vsu.cs.proskuryakov.coffeestrike.app.services.ProductService;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.ProductItem;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductAPI {

    private final ProductService productService;
    private final MapperFacade mapper;

    @Override
    public ResponseEntity<Product> create(Product params) {
        return Optional.of(params)
                .map(p -> mapper.map(p, ProductItem.class))
                .map(productService::create)
                .map(p -> mapper.map(p, Product.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<List<Product>> getAll() {
        return Optional.of(productService.getAll())
                .map(pl -> mapper.mapAsList(pl, Product.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<List<Product>> search(String name) {
        return Optional.of(productService.search(name))
                .map(pl -> mapper.mapAsList(pl, Product.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
