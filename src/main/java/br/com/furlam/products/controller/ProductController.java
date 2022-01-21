package br.com.furlam.products.controller;

import br.com.furlam.products.enums.EventType;
import br.com.furlam.products.model.Product;
import br.com.furlam.products.repository.ProductRepository;
import br.com.furlam.products.service.ProductPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductRepository productRepository;
    private ProductPublisher productPublisher;

    @Autowired
    public ProductController(ProductRepository productRepository, ProductPublisher productPublisher) {
        this.productRepository = productRepository;
        this.productPublisher = productPublisher;
    }

    @GetMapping
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product productCreated = productRepository.save(product);

        productPublisher.publishProductEvent(productCreated, EventType.PRODUCT_CREATED, "matilde");
        return new ResponseEntity<>(productCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);

            Product productUpdated = productRepository.save(product);

            productPublisher.publishProductEvent(productUpdated, EventType.PRODUCT_UPDATE, "doralice");

            return new ResponseEntity<>(productUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();

            productRepository.delete(product);

            productPublisher.publishProductEvent(product, EventType.PRODUCT_DELETED, "hannah");

            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCode")
    public ResponseEntity<Product> findByCode(@RequestParam String code) {
        Optional<Product> optProduct = productRepository.findByCode(code);
        return optProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
