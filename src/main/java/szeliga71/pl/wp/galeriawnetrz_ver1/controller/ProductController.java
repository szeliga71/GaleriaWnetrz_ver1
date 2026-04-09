package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor // Automatyczny konstruktor dla productService
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(productService.getAllProducts(page, size, sort));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        // Poprawka: mapujemy Optional na ResponseEntity
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable("name") String productName) {
        return ResponseEntity.ok(productService.getProductByName(productName));
    }

    @GetMapping("/brand/name/{brandName}")
    public ResponseEntity<List<ProductDto>> getProductsByBrandName(
            @PathVariable String brandName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(productService.getProductsByBrandName(brandName, page, size, sort));
    }

    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(productService.getProductsByCategoryName(categoryName, page, size, sort));
    }

    @GetMapping("/subcategory/name/{subCategoryName}")
    public ResponseEntity<List<ProductDto>> getProductsBySubCategoryName(
            @PathVariable("subCategoryName") String subCategoryName) {
        return ResponseEntity.ok(productService.getProductsBySubCategoryName(subCategoryName));
    }

    // --- COUNTERS (Liczniki zwracają proste typy, więc ResponseEntity.ok jest opcjonalne, ale zalecane) ---

    @GetMapping("/brand/name/{brandName}/count")
    public ResponseEntity<Long> countProductsByBrandName(@PathVariable String brandName) {
        return ResponseEntity.ok(productService.countProductsByBrandName(brandName));
    }

    @GetMapping("/category/name/{categoryName}/count")
    public ResponseEntity<Long> countProductsByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.countProductsByCategoryName(categoryName));
    }

    @GetMapping("/subcategory/name/{subCategoryName}/count")
    public ResponseEntity<Long> countProductsBySubCategoryName(@PathVariable String subCategoryName) {
        return ResponseEntity.ok(productService.countProductsBySubCategoryName(subCategoryName));
    }
}