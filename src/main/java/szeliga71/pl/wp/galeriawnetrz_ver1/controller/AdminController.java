package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BrandService brandService;
    private final CategorieService categorieService;
    private final ProductService productService;
    private final SubCategoriesService subCategoriesService;
    private final PostsService postsService;

    @Autowired
    public AdminController(BrandService brandService, CategorieService categorieService, ProductService productService, SubCategoriesService subCategoriesService, PostsService postsService) {
        this.brandService = brandService;
        this.categorieService = categorieService;
        this.productService = productService;
        this.subCategoriesService = subCategoriesService;
        this.postsService = postsService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PostMapping("/brand")
    public ResponseEntity<BrandDto> createProduct(@RequestBody BrandDto brandDto) {
        BrandDto savedBrand = brandService.saveBrand(brandDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    @PutMapping("/brand/{id}")
    public ResponseEntity<BrandDto> updateBrand(
            @PathVariable Long id,
            @RequestBody BrandDto dto) {

        Optional<BrandDto> existing = brandService.getBrandById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        dto.setBrandId(id);
        BrandDto updated = brandService.saveBrand(dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/brand/{id}")
    public ResponseEntity<BrandDto> patchBrand(
            @PathVariable Long id,
            @RequestBody BrandUpdateDto updates) {

        Optional<BrandDto> existingOpt = brandService.getBrandById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        BrandDto existing = existingOpt.get();

        if (updates.getBrandName() != null) existing.setBrandName(updates.getBrandName());
        if (updates.getBrandUrl() != null) existing.setBrandUrl(updates.getBrandUrl());
        if (updates.getBrandDescriptionENG() != null) existing.setBrandDescriptionENG(updates.getBrandDescriptionENG());
        if (updates.getBrandDescriptionPL() != null) existing.setBrandDescriptionPL(updates.getBrandDescriptionPL());
        if (updates.getBrandImageUrl() != null) existing.setBrandImageUrl(updates.getBrandImageUrl());

        existing.setBrandId(id);

        BrandDto updated = brandService.saveBrand(existing);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoriesDto> createCategory(@RequestBody CategoriesDto categoryDto) {
        CategoriesDto savedCategory = categorieService.saveCategory(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }


    @PostMapping("/subcategory")
    public ResponseEntity<SubCategoriesDto> createSubCategory(@RequestBody SubCategoriesDto dto) {
        // Walidacja categoryId - upewniamy się, że nie jest null ani 0
        if (dto.getCategoryId() == null || dto.getCategoryId() == 0) {
            throw new RuntimeException("categoryId must be provided and greater than 0");
        }

        SubCategoriesDto saved = subCategoriesService.saveSubCategory(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto dto) {

        Optional<ProductDto> existing = productService.getProductById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        dto.setProductId(id);
        ProductDto updated = productService.saveProduct(dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/product/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateDto updates) {

        Optional<ProductDto> existingOpt = productService.getProductById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProductDto existing = existingOpt.get();

        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getPdfUrl() != null) existing.setPdfUrl(updates.getPdfUrl());
        if (updates.getBrandId() != null) existing.setBrandId(updates.getBrandId());
        if (updates.getCategoryId() != null) existing.setCategoryId(updates.getCategoryId());
        if (updates.getSubCategoryId() != null) existing.setSubCategoryId(updates.getSubCategoryId());
        if (updates.getDescriptionENG() != null) existing.setDescriptionENG(updates.getDescriptionENG());
        if (updates.getDescriptionPL() != null) existing.setDescriptionPL(updates.getDescriptionPL());
        if (updates.getImages() != null) existing.setImages(updates.getImages());

        existing.setProductId(id);

        ProductDto updated = productService.saveProduct(existing);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }

    @DeleteMapping("/products")
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @PostMapping(
            value = "/import/products",
            consumes = {"multipart/form-data"}
    )

    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        int count = productService.importProductsFromCsv(file);
        return ResponseEntity.ok("Imported " + count + " products");
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categorieService.deleteCategory(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }

    @DeleteMapping("/categories")
    public ResponseEntity<Void> deleteAllCategories() {
        categorieService.deleteAllCategories();
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @DeleteMapping("/subCategory/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        subCategoriesService.deleteSubCategory(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }

    @DeleteMapping("/subCategories")
    public ResponseEntity<Void> deleteAllSubCategories() {
        subCategoriesService.deleteAllSubCategories();
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @DeleteMapping("/brand/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }

    @DeleteMapping("/brands")
    public ResponseEntity<Void> deleteAllBrands() {
        brandService.deleteAllBrands();
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
