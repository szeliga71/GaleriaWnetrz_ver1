package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.CategoryService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryRepo categoryRepo;
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryRepo categoryRepo, CategoryService categoryService) {
        this.categoryRepo = categoryRepo;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/by-name/{categoryName}")
    public Optional<Category> getCategoryByName(@PathVariable String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    @GetMapping("/id/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepo.findById(id);
    }
}

