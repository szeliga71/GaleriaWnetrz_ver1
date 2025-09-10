package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductsRepo productsRepo;

    private final ProductService productService;

    @Autowired
    public ProductsController(ProductsRepo productsRepo, ProductService productService) {
        this.productsRepo = productsRepo;
        this.productService = productService;
    }
    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
    @GetMapping("/subcategory/{subCategoryId}")
    public List<ProductDto> getProductsBySubCategory(@PathVariable Long subCategoryId) {
        return productService.getProductsBySubCategory(subCategoryId);
    }
    @GetMapping("/brand/{brandId}")
    public List<ProductDto> getProductsByBrand(@PathVariable Long brandId) {
        return productService.getProductsByBrand(brandId);
    }
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/{id}")
    public Optional<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

   /* @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }*/


}
