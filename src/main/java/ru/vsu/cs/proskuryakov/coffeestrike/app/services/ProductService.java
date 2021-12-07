package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.ProductItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SequenceService sequenceService;

    public ProductItem create(ProductItem productItem) {
        return productRepository.insert(productItem.withProductid(sequenceService.getNextProductId()));
    }

    public List<ProductItem> getAll() {
        return productRepository.findAll();
    }

    public List<ProductItem> search(String name) {
        return productRepository.findAllByNameContains(name.toLowerCase());
    }
}
