package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.CategorieService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesRepo categoriesRepo;
    private final CategorieService categorieService;

    @Autowired
    public CategoriesController(CategoriesRepo categoriesRepo,CategorieService categorieService) {
        this.categoriesRepo = categoriesRepo;
        this.categorieService = categorieService;
    }

    @GetMapping
    public List<Categories> getAllCategories() {
        return categoriesRepo.findAll();
    }


    @GetMapping("/{id}")
    public Optional<Categories> getCategoryById(@PathVariable Long id) {
        return categoriesRepo.findById(id);
    }



    /*@PostMapping
    public ResponseEntity<CategoriesDto> createCategory(@RequestBody CategoriesDto categoryDto) {
        CategoriesDto savedCategory = categorieService.saveCategory(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }*/
}

