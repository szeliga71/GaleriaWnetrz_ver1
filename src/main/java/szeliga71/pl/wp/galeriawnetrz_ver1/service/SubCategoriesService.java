package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoriesRepo;

import java.util.List;

@Service
@Transactional
public class SubCategoriesService {

    private final SubCategoriesRepo subCategoriesRepo;
    private final CategoriesRepo categoriesRepo;

    public SubCategoriesService(SubCategoriesRepo subCategoriesRepo, CategoriesRepo categoriesRepo) {
        this.subCategoriesRepo = subCategoriesRepo;
        this.categoriesRepo = categoriesRepo;
    }

    public List<SubCategories> getAllSubCategories() {
        return subCategoriesRepo.findAll();
    }


    public SubCategories getSubCategoryById(Long id) {
        return subCategoriesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
    }


    public List<SubCategories> getByCategoryId(Long categoryId) {
        return subCategoriesRepo.findByCategory_CategoryId(categoryId);
    }

    public SubCategoriesDto saveSubCategory(SubCategoriesDto dto) {
        Categories category = categoriesRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        SubCategories subCategory = new SubCategories();
        subCategory.setSubCategoryName(dto.getSubCategoryName());
        subCategory.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        subCategory.setCategory(category);

        SubCategories saved = subCategoriesRepo.save(subCategory);
        return mapToDto(saved);
    }

    public SubCategoriesDto updateSubCategory(Long id, SubCategoriesDto dto) {
        SubCategories subCategory = getSubCategoryById(id);
        Categories category = categoriesRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        subCategory.setSubCategoryName(dto.getSubCategoryName());
        subCategory.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        subCategory.setCategory(category);

        SubCategories saved = subCategoriesRepo.save(subCategory);
        return mapToDto(saved);
    }

    public void deleteSubCategory(Long id) {
        SubCategories subCategory = subCategoriesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Categories parent = subCategory.getCategory();
        if (parent != null) {
            parent.getSubCategories().remove(subCategory);
        }

        subCategoriesRepo.delete(subCategory);
    }

    public void deleteAllSubCategories() {
        subCategoriesRepo.deleteAll();
    }

    private SubCategoriesDto mapToDto(SubCategories entity) {
        SubCategoriesDto dto = new SubCategoriesDto();
        dto.setSubCategoryId(entity.getSubCategoryId());
        dto.setSubCategoryName(entity.getSubCategoryName());
        dto.setSubCategoryImageUrl(entity.getSubCategoryImageUrl());
        dto.setSlugSubCategoryName(entity.getSlugSubCategoryName());
        dto.setCategoryId(entity.getCategory().getCategoryId());
        return dto;
    }

    private SubCategories mapToEntity(SubCategoriesDto dto) {
        SubCategories entity = new SubCategories();
        entity.setSubCategoryName(dto.getSubCategoryName());
        entity.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        entity.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        return entity;
    }
}



