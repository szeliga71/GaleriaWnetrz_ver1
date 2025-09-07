package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductsRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductsRepo productsRepo;

    @Autowired
    public ProductsController(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    @GetMapping("/{id}")
    public Optional<Products> getProductById(@PathVariable Long id) {
        return productsRepo.findById(id);
    }

   @GetMapping("/brand/{brandId}")
    public Optional<Products> getProductsByBrand(@PathVariable Long brandId) {
        return productsRepo.findByBrandId(brandId);
    }

    @GetMapping("/category/{categoryId}")
    public Optional<Products> getProductsByCategory(@PathVariable Long categoryId) {
        return productsRepo.findByCategoryId(categoryId);
    }

    @GetMapping
    public List<Products> getAllProducts() {
        return productsRepo.findAll();
    }
}
