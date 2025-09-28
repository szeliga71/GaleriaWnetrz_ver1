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
    private final CategoryService categoryService;
    private final ProductService productService;
    private final SubCategoryService subCategoryService;
    private final PostsService postsService;

    @Autowired
    public AdminController(BrandService brandService, CategoryService categoryService, ProductService productService, SubCategoryService subCategoryService, PostsService postsService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.subCategoryService = subCategoryService;
        this.postsService = postsService;
    }


    //================================PRODUCT=============================================

    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
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


    @PatchMapping("/productId/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Long id,
            @RequestBody ProductDto updates) {

        return productService.patchProduct(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/product/by-name/{productName}")
    public ResponseEntity<ProductDto> updateProductByName(
            @PathVariable String productName,
            @RequestBody ProductDto dto) {
        try {
            ProductDto updated = productService.updateProductByName(productName, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/product/by-name/{productName}")
    public ResponseEntity<ProductDto> patchProductByName(
            @PathVariable String productName,
            @RequestBody ProductDto updates) {
        return productService.patchProductByName(productName, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }
    @DeleteMapping("/product/by-name/{productName}")
    public ResponseEntity<Void> deleteProductByName(@PathVariable String productName) {
        try {
            productService.deleteProductByName(productName);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/products/reset")
    public ResponseEntity<Void> deleteAllProductsAndReset() {
        productService.deleteAllAndReset();
        return ResponseEntity.noContent().build(); // HTTP 204
    }



 //=================================BRANDS===========================================
   @PostMapping("/brand")
   public ResponseEntity<BrandDto> createBrand(@RequestBody BrandCreateDto dto) {
       BrandDto saved = brandService.createBrandFromDto(dto);
       return ResponseEntity.status(HttpStatus.CREATED).body(saved);
   }


    @PutMapping("/brand/{id}")
    public ResponseEntity<BrandDto> updateBrand(
            @PathVariable Long id,
            @RequestBody BrandCreateDto dto) {

        try {
            BrandDto updated = brandService.updateBrand(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/brand/{id}")
    public ResponseEntity<BrandDto> patchBrand(
            @PathVariable Long id,
            @RequestBody BrandCreateDto updates) {

        return brandService.patchBrand(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/brand/by-name/{brandName}")
    public ResponseEntity<BrandDto> updateBrandByName(
            @PathVariable String brandName,
            @RequestBody BrandCreateDto dto) {
        try {
            BrandDto updated = brandService.updateBrandByName(brandName, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/brand/by-name/{brandName}")
    public ResponseEntity<BrandDto> patchBrandByName(
            @PathVariable String brandName,
            @RequestBody BrandCreateDto updates) {
        return brandService.patchBrandByName(brandName, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/brands/reset")
    public ResponseEntity<Void> deleteAllBrandsAndReset() {
        brandService.deleteAllAndReset();
        return ResponseEntity.noContent().build(); // HTTP 204
    }
    @DeleteMapping("/brand/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }
    @DeleteMapping("/brand/by-name/{brandName}")
    public ResponseEntity<Void> deleteBrandByName(@PathVariable String brandName) {
        try {
            brandService.deleteBrandByName(brandName);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    //================================CATEGORIES================================================

    @PostMapping("/category")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }
    @DeleteMapping("/category/by-name/{categoryName}")
    public ResponseEntity<Void> deleteCategoryByName(@PathVariable String categoryName) {
        categoryService.deleteCategoryByCategoryName(categoryName);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }

    @DeleteMapping("/category/reset")
    public ResponseEntity<Void> deleteAllCategoryReset() {
        categoryService.deleteAllAndReset();
        return ResponseEntity.noContent().build(); // HTTP 204
    }


    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto dto) {

        try {
            CategoryDto updated = categoryService.updateCategory(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity<CategoryDto> patchCategories(
            @PathVariable Long id,
            @RequestBody CategoryDto updates) {

        return categoryService.patchCategory(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/category/by-name/{categoryName}")
    public ResponseEntity<CategoryDto> updateCategoryByName(
            @PathVariable String categoryName,
            @RequestBody CategoryDto dto) {
        try {

            CategoryDto updated = categoryService.updateCategoryByName(categoryName, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/category/by-name/{categoryName}")
    public ResponseEntity<CategoryDto> patchCategoryByName(
            @PathVariable String categoryName,
            @RequestBody CategoryDto updates) {
        return categoryService.patchCategoryByName(categoryName, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


//========================SUBCATEGORY===========================================

    @DeleteMapping("/subCategory/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }
    @DeleteMapping("/subCategory/reset")
    public ResponseEntity<Void> deleteAllSubCategoryReset() {
        subCategoryService.deleteAllSubCategoryAndReset();
        return ResponseEntity.noContent().build(); // HTTP 204
    }
    @DeleteMapping("/subCategory/by-name/{subCategoryName}")
    public ResponseEntity<Void> deleteSubCategoryByName(@PathVariable String subCategoryName) {
        subCategoryService.deleteSubCategoryBySubCategoryName(subCategoryName);
        return ResponseEntity.noContent().build(); // zwraca status 204
    }


    @PostMapping("/subcategory")
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody SubCategoryDto dto) {
        // Walidacja categoryId - upewniamy się, że nie jest null ani 0
        if (dto.getCategoryId() == null || dto.getCategoryId() == 0) {
            throw new RuntimeException("CategoryId must be provided and greater than 0");
        }

        SubCategoryDto saved = subCategoryService.saveSubCategory(dto);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/subCategory/{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryDto dto) {

        try {
            SubCategoryDto updated = subCategoryService.updateSubCategory(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/subCategory/{id}")
    public ResponseEntity<SubCategoryDto> patchSubCategories(
            @PathVariable Long id,
            @RequestBody SubCategoryDto updates) {

        return subCategoryService.patchSubCategory(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/subCategory/by-name/{subCategoryName}")
    public ResponseEntity<SubCategoryDto> updateSubCategoryByName(
            @PathVariable String subCategoryName,
            @RequestBody SubCategoryDto dto) {
        try {

            SubCategoryDto updated = subCategoryService.updateSubCategoryByName(subCategoryName, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/subCategory/by-name/{subCategoryName}")
    public ResponseEntity<SubCategoryDto> patchSubCategoryByName(
            @PathVariable String subCategoryName,
            @RequestBody SubCategoryDto updates) {
        return subCategoryService.patchSubCategoryByName(subCategoryName, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
