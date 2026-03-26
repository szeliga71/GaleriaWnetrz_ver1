/*package szeliga71.pl.wp.galeriawnetrz_ver1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductService productService;

    @GetMapping("/category/name/{categoryName}")
    public List<ProductDto> getProductsByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getProductsByCategoryName(categoryName, page, size, sort);
    }

    @GetMapping("/subcategory/name/{subCategoryName}")
    public List<ProductDto> getProductsBySubCategoryName(@PathVariable("subCategoryName") String subCategoryName) {
        return productService.getProductsBySubCategoryName(subCategoryName);
    }

    @GetMapping("/brand/name/{brandName}")
    public List<ProductDto> getProductsByBrandName(
            @PathVariable String brandName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getProductsByBrandName(brandName, page, size, sort);
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getAllProducts(page, size, sort);
    }
    @GetMapping("/id/{id}")
    public Optional<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/name/{name}")
    public List<ProductDto> getProductByName(@PathVariable("name") String productName) {
        return productService.getProductByName(productName);
    }

    /*@GetMapping("/products/category/name/{categoryName}/subcategory/name/{subCategoryName}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryNameAndSubCategoryName(
            @PathVariable ("categoryName")String categoryName,
            @PathVariable ("subCategoryName")String subCategoryName) {
        List<ProductDto> products = productService.getProductsByCategoryNameAndSubCategoryName(categoryName, subCategoryName);
        return ResponseEntity.ok(products);
    }*/
    //  zliczanie
    // Liczba produktów dla brandu
    /*@GetMapping("/brand/name/{brandName}/count")
    public long countProductsByBrandName(@PathVariable String brandName) {
        return productService.countProductsByBrandName(brandName);
    }
    // Liczba produktów dla kategorii
    @GetMapping("/category/name/{categoryName}/count")
    public long countProductsByCategoryName(@PathVariable String categoryName) {
        return productService.countProductsByCategoryName(categoryName);
    }
    // Liczba produktów dla podkategorii
    @GetMapping("/subcategory/name/{subCategoryName}/count")
    public long countProductsBySubCategoryName(@PathVariable String subCategoryName) {
        return productService.countProductsBySubCategoryName(subCategoryName);
    }
}*/
package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getAllProducts(page, size, sort);
    }

    @GetMapping("/id/{id}")
    public Optional<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/name/{name}")
    public List<ProductDto> getProductByName(@PathVariable("name") String productName) {
        return productService.getProductByName(productName);
    }

    @GetMapping("/brand/name/{brandName}")
    public List<ProductDto> getProductsByBrandName(
            @PathVariable String brandName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getProductsByBrandName(brandName, page, size, sort);
    }

    @GetMapping("/category/name/{categoryName}")
    public List<ProductDto> getProductsByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        return productService.getProductsByCategoryName(categoryName, page, size, sort);
    }

    @GetMapping("/subcategory/name/{subCategoryName}")
    public List<ProductDto> getProductsBySubCategoryName(
            @PathVariable("subCategoryName") String subCategoryName) {
        return productService.getProductsBySubCategoryName(subCategoryName);
    }

    // COUNTERS
    @GetMapping("/brand/name/{brandName}/count")
    public long countProductsByBrandName(@PathVariable String brandName) {
        return productService.countProductsByBrandName(brandName);
    }

    @GetMapping("/category/name/{categoryName}/count")
    public long countProductsByCategoryName(@PathVariable String categoryName) {
        return productService.countProductsByCategoryName(categoryName);
    }

    @GetMapping("/subcategory/name/{subCategoryName}/count")
    public long countProductsBySubCategoryName(@PathVariable String subCategoryName) {
        return productService.countProductsBySubCategoryName(subCategoryName);
    }
}

