package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor // Automatyczny konstruktor dla pól final
public class AdminController {

    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final SubCategoryService subCategoryService;
    private final PostService postService;
    private final QueryService queryService;

    //============================== SQL ====================================
    @PostMapping("/sql")
    public ResponseEntity<List<Object[]>> execute(@RequestBody SqlRequestDto request) {
        List<Object[]> result = queryService.runSql(request.getQuery());
        return ResponseEntity.ok(result);
    }

    //============================== PRODUCT =============================================

    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateDto productDto) {
        ProductDto savedProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductCreateDto dto) {
        ProductDto updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/products/reset")
    public ResponseEntity<Void> deleteAllProductsAndReset() {
        productService.deleteAllAndReset();
        return ResponseEntity.noContent().build();
    }

    //================================= BRANDS ===========================================
    @PostMapping("/brand")
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandCreateDto dto) {
        BrandDto saved = brandService.createBrandFromDto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/brand/{id}")
    public ResponseEntity<BrandDto> updateBrand(@PathVariable Long id, @RequestBody BrandCreateDto dto) {
        BrandDto updated = brandService.updateBrand(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/brand/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/brands/reset")
    public ResponseEntity<Void> deleteAllBrandsAndReset() {
        brandService.deleteAllAndReset();
        return ResponseEntity.noContent().build();
    }

    //================================ CATEGORIES ================================================

    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }
    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        // Zakładamy, że CategoryService.updateCategory(Long id, CategoryDto dto) istnieje
        CategoryDto updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        // Sprawdź czy w CategoryService metoda nazywa się deleteCategory czy deleteCategoryById
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }



    //======================== SUBCATEGORY ===========================================

    @PostMapping("/subcategory")
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto dto) {
        if (dto.getCategoryId() == null || dto.getCategoryId() == 0) {
            return ResponseEntity.badRequest().build();
        }
        SubCategoryDto saved = subCategoryService.saveSubCategory(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/subCategory/{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@PathVariable Long id, @RequestBody SubCategoryDto dto) {
        SubCategoryDto updated = subCategoryService.updateSubCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/subCategory/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subCategory/reset")
    public ResponseEntity<Void> deleteAllSubCategoryReset() {
        subCategoryService.deleteAllSubCategoryAndReset();
        return ResponseEntity.noContent().build();
    }
}