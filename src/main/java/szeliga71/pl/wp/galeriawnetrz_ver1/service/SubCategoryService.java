package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;

import java.util.List;

@Service
@Transactional
public class SubCategoryService {

    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;

    public SubCategoryService(SubCategoryRepo subCategoryRepo, CategoryRepo categoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<SubCategory> getAllSubCategory() {
        return subCategoryRepo.findAll();
    }


    public void deleteAllSubCategoryAndReset() {
        subCategoryRepo.truncateSubCategory();
    }


    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
    }


    public List<SubCategory> getByCategoryId(Long categoryId) {
        return subCategoryRepo.findByCategory_CategoryId(categoryId);
    }

    public SubCategoryDto saveSubCategory(SubCategoryDto dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(dto.getSubCategoryName());
        subCategory.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        subCategory.setCategory(category);

        SubCategory saved = subCategoryRepo.save(subCategory);
        return mapToDto(saved);
    }

    public SubCategoryDto updateSubCategory(Long id, SubCategoryDto dto) {
        SubCategory subCategory = getSubCategoryById(id);
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        subCategory.setSubCategoryName(dto.getSubCategoryName());
        subCategory.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        subCategory.setCategory(category);

        SubCategory saved = subCategoryRepo.save(subCategory);
        return mapToDto(saved);
    }

    public void deleteSubCategory(Long id) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Category parent = subCategory.getCategory();
        if (parent != null) {
            parent.getSubCategories().remove(subCategory);
        }

        subCategoryRepo.delete(subCategory);
    }

    public void deleteAllSubCategories() {
        subCategoryRepo.deleteAll();
    }

    private SubCategoryDto mapToDto(SubCategory entity) {
        SubCategoryDto dto = new SubCategoryDto();
        dto.setSubCategoryId(entity.getSubCategoryId());
        dto.setSubCategoryName(entity.getSubCategoryName());
        dto.setSubCategoryImageUrl(entity.getSubCategoryImageUrl());
        dto.setSlugSubCategoryName(entity.getSlugSubCategoryName());
        dto.setCategoryId(entity.getCategory().getCategoryId());
        return dto;
    }

    private SubCategory mapToEntity(SubCategoryDto dto) {
        SubCategory entity = new SubCategory();
        entity.setSubCategoryName(dto.getSubCategoryName());
        entity.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        entity.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        return entity;
    }
}



