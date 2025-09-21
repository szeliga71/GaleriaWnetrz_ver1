package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
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
    @GetMapping("/category/id/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
    @GetMapping("/category/name/{name}")
    public List<ProductDto> getProductsByCategoryName(@PathVariable String name) {
        return productService.getProductsByCategoryName(name);
    }

    @GetMapping("/subcategory/id/{subCategoryId}")
    public List<ProductDto> getProductsBySubCategory(@PathVariable Long subCategoryId) {
        return productService.getProductsBySubCategory(subCategoryId);
    }
    @GetMapping("/subcategory/name/{subCategoryName}")
    public List<ProductDto> getProductsBySubCategoryName(@PathVariable("subCategoryName") String subCategoryName) {
        return productService.getProductsBySubCategoryName(subCategoryName);
    }
    @GetMapping("/brand/id/{brandId}")
    public List<ProductDto> getProductsByBrand(@PathVariable Long brandId) {
        return productService.getProductsByBrandId(brandId);
    }
    @GetMapping("/brand/name/{brandName}")
   public List<ProductDto> getProductsByBrand(@PathVariable("brandName") String brandName) {
       return productService.getProductsByBrandName(brandName);
   }
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/id/{id}")
    public Optional<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @GetMapping("/name/{name}")
    public List<ProductDto> getProductByName(@PathVariable("name") String productName) {
        return productService.getProductByName(productName);
    }

    @GetMapping("/products/category/{categoryId}/subcategory/{subCategoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryAndSubCategory(
            @PathVariable Long categoryId,
            @PathVariable Long subCategoryId) {
        List<ProductDto> products = productService.getProductsByCategoryAndSubCategory(categoryId, subCategoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/category/name/{categoryName}/subcategory/name/{subCategoryName}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryNameAndSubCategoryName(
            @PathVariable ("categoryName")String categoryName,
            @PathVariable ("subCategoryName")String subCategoryName) {
        List<ProductDto> products = productService.getProductsByCategoryNameAndSubCategoryName(categoryName, subCategoryName);
        return ResponseEntity.ok(products);
    }





}
