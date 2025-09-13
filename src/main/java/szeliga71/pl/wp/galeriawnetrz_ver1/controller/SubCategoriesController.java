package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoriesRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.SubCategoriesService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/subcategories")
public class SubCategoriesController {


    private final SubCategoriesService subCategoriesService;
    private final SubCategoriesRepo subCategoriesRepo;


    @Autowired
    public SubCategoriesController(SubCategoriesRepo subCategoriesRepo, SubCategoriesService subCategoriesService) {
        this.subCategoriesRepo = subCategoriesRepo;
        this.subCategoriesService = subCategoriesService;
    }

    /*@GetMapping
    public List<SubCategories> getAllSubCategories() {
        return subCategoriesRepo.findAll();
    }*/


    @GetMapping("/{id}")
    public Optional<SubCategories> getSubCategoryById(@PathVariable Long id) {
        return subCategoriesRepo.findById(id);
    }

    @GetMapping("/subcategories")
    public ResponseEntity<List<SubCategoriesDto>> getAllSubCategories() {
        List<SubCategoriesDto> list = subCategoriesService.getAllSubCategories()
                .stream()
                .map(s -> {
                    SubCategoriesDto dto = new SubCategoriesDto();
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

    /*@PostMapping
    public ResponseEntity<SubCategoriesDto> createSubCategory(@RequestBody SubCategoriesDto dto) {
        SubCategoriesDto saved = subCategoriesService.saveSubCategory(dto);
        return ResponseEntity.ok(saved);
    }*/
}
