package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.BrandDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.*;

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
    @PostMapping("/category")
    public ResponseEntity<CategoriesDto> createCategory(@RequestBody CategoriesDto categoryDto) {
        CategoriesDto savedCategory = categorieService.saveCategory(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }


    @PostMapping("/subcategory")
    public ResponseEntity<SubCategoriesDto> createSubCategory(@RequestBody SubCategoriesDto dto) {
        SubCategoriesDto saved = subCategoriesService.saveSubCategory(dto);
        return ResponseEntity.ok(saved);
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
