package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.SubCategoryService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/subcategory")
public class SubCategoryController {


    private final SubCategoryService subCategoryService;
    private final SubCategoryRepo subCategoryRepo;


    @Autowired
    public SubCategoryController(SubCategoryRepo subCategoryRepo, SubCategoryService subCategoryService) {
        this.subCategoryRepo = subCategoryRepo;
        this.subCategoryService = subCategoryService;
    }


    @GetMapping("/id/{id}")
    public Optional<SubCategory> getSubCategoryById(@PathVariable Long id) {
        return subCategoryRepo.findById(id);
    }


    @GetMapping("/all")
    public List<SubCategory> getAllSubCategory() {
        return subCategoryRepo.findAll();
    }
    @GetMapping("/by-name/{subCategoryName}")
    public Optional<SubCategory> getSubCategoryByName(@PathVariable String subCategoryName) {
        return subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName);
    }

    @GetMapping("/category/by-subCategoryName/{subCategoryName}")
        public Optional<Category>getCategoryBySubCategoryName(@PathVariable String subCategoryName){
        return subCategoryService.getCategoryBySubCategoryName(subCategoryName);
    }

    @GetMapping("/subCategory/by-categoryName/{categoryName}")
    public List<SubCategory>getSubCategoryByCategoryName(@PathVariable String categoryName){
        return subCategoryService.getSubCategoryByCategoryName(categoryName);
    }
    @GetMapping("/subCategory/by-categoryId/{categoryId}")
    public List<SubCategory>getSubCategoryByCategoryId(@PathVariable Long categoryId){
        return subCategoryService.getSubCategoryByCategoryId(categoryId);
    }

}
