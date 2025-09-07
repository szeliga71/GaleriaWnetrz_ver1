package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesRepo categoriesRepo;

    @Autowired
    public CategoriesController(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }

    @GetMapping
    public List<Categories> getAllCategories() {
        return categoriesRepo.findAll();
    }


    @GetMapping("/{id}")
    public Optional<Categories> getCategoryById(@PathVariable Long id) {
        return categoriesRepo.findById(id);
    }

    @GetMapping("/subcategories")
    public List<Categories> getAllSubCategories() {
        return categoriesRepo.findAll();
    }


    @GetMapping("/subcategories/{id}")
    public Optional<Categories> getSubcategoryById(@PathVariable Long id) {
        return categoriesRepo.findById(id);
    }
}

