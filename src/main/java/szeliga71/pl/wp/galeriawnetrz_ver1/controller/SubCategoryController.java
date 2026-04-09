package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.SubCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
@RequiredArgsConstructor // Automatyczny konstruktor (zastępuje @Autowired)
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    // Pobieranie wszystkich (zmienione na DTO dla bezpieczeństwa)
    @GetMapping("/all")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    // Pobieranie po ID
    @GetMapping("/id/{id}")
    public ResponseEntity<SubCategoryDto> getSubCategoryById(@PathVariable Long id) {
        // Zakładamy, że w serwisie masz metodę zwracającą Optional<SubCategoryDto>
        // Jeśli zwraca SubCategory, użyj: Optional.ofNullable(service.getById(id)).map...
        return ResponseEntity.ok(subCategoryService.mapToDto(subCategoryService.getSubCategoryById(id)));
    }

    // Pobieranie po nazwie (NAPRAWIONY BŁĄD)
    @GetMapping("/by-name/{subCategoryName}")
    public ResponseEntity<SubCategoryDto> getSubCategoryByName(@PathVariable String subCategoryName) {
        // Używamy serwisu zamiast repozytorium bezpośrednio
        // Metoda w repozytorium powinna nazywać się findByNameIgnoreCase
        return subCategoryService.getSubCategoryByName(subCategoryName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/by-subCategoryName/{subCategoryName}")
    public ResponseEntity<CategoryDto> getCategoryBySubCategoryName(@PathVariable String subCategoryName) {
        // Tu musisz upewnić się, że serwis zwraca Optional<CategoryDto>
        return subCategoryService.getCategoryBySubCategoryNameDto(subCategoryName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-category-name/{categoryName}")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoryByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryByCategoryNameDto(categoryName));
    }

    @GetMapping("/by-category-id/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoryByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryByCategoryIdDto(categoryId));
    }
}