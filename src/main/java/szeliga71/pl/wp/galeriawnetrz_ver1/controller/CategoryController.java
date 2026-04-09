package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor // Automatyczny konstruktor dla categoryService
public class CategoryController {

    private final CategoryService categoryService;

    // 🔹 Pobieranie wszystkich kategorii (zmapowanych na DTO)
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // 🔹 Pobieranie po ID
    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        // Zakładamy, że w CategoryService masz metodę getCategoryById zwracającą Optional<CategoryDto>
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Pobieranie po nazwie (Naprawiony błąd nazewnictwa)
    @GetMapping("/by-name/{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name) {
        // Zakładamy, że w CategoryService masz metodę getCategoryByName
        return categoryService.getCategoryByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
