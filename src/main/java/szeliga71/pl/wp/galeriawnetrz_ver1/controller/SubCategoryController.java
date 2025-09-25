package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/subcategory")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        List<SubCategoryDto> list = subCategoryService.getAllSubCategory()
                .stream()
                .map(s -> {
                    SubCategoryDto dto = new SubCategoryDto();
                    dto.setSubCategoryId(s.getSubCategoryId());
                    dto.setSubCategoryName(s.getSubCategoryName());
                    dto.setSubCategoryImageUrl(s.getSubCategoryImageUrl());
                    dto.setSlugSubCategoryName(s.getSlugSubCategoryName());
                    dto.setCategoryId(s.getCategory().getCategoryId());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-name/{subCategoryName}")
    public Optional<SubCategory> getSubCategoryByName(@PathVariable String subCategoryName) {
        return subCategoryService.getSubCategoryByName(subCategoryName);
    }

}
