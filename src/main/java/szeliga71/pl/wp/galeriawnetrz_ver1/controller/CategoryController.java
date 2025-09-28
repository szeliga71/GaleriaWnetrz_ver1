package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


    public CategoryController(CategoryRepo categoryRepo, CategoryService categoryService) {
        this.categoryRepo = categoryRepo;
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }

    @GetMapping("/by-name/{categoryName}")
    public Optional<Category> getCategoryByName(@PathVariable String categoryName) {
        return categoryRepo.findByCategoryNameIgnoreCase(categoryName);
    }

    @GetMapping("/id/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepo.findById(id);
    }
}

