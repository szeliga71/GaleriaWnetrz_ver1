package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;

    public void deleteAllSubCategoryAndReset() {
        subCategoryRepo.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<SubCategoryDto> getAllSubCategories() {
        return subCategoryRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepo.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<SubCategoryDto> getSubCategoryByName(String name) {
        return subCategoryRepo.findByNameIgnoreCase(name)
                .map(this::mapToDto);
    }

    public SubCategoryDto saveSubCategory(SubCategoryDto dto) {
        Category category = categoryRepo.findById(dto.getCategoryId()).orElse(null);

        SubCategory subCategory = new SubCategory();
        subCategory.setName(dto.getSubCategoryName());
        subCategory.setImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlug(generateSlug(dto.getSubCategoryName()));
        subCategory.setCategory(category);

        SubCategory saved = subCategoryRepo.save(subCategory);
        return mapToDto(saved);
    }

    public void deleteSubCategory(Long id) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Category parent = subCategory.getCategory();
        if (parent != null && parent.getSubCategories() != null) {
            parent.getSubCategories().remove(subCategory);
        }
        subCategoryRepo.delete(subCategory);
    }

    // ZMIENIONE NA PUBLIC - Kontroler musi mieć do tego dostęp
    public SubCategoryDto mapToDto(SubCategory entity) {
        if (entity == null) return null;
        SubCategoryDto dto = new SubCategoryDto();
        dto.setSubCategoryId(entity.getId());
        dto.setSubCategoryName(entity.getName());
        dto.setSubCategoryImageUrl(entity.getImageUrl());
        dto.setSlugSubCategoryName(entity.getSlug());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
        }
        return dto;
    }

    @Transactional
    public void deleteSubCategoryBySubCategoryName(String name) {
        SubCategory existing = subCategoryRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Category parent = existing.getCategory();
        if (parent != null && parent.getSubCategories() != null) {
            parent.getSubCategories().remove(existing);
        }
        subCategoryRepo.delete(existing);
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    @Transactional
    public SubCategoryDto updateSubCategory(Long id, SubCategoryDto dto) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Category category = categoryRepo.findById(dto.getCategoryId()).orElse(null);

        subCategory.setName(dto.getSubCategoryName());
        subCategory.setImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlug(dto.getSlugSubCategoryName() != null ? dto.getSlugSubCategoryName() : generateSlug(dto.getSubCategoryName()));
        subCategory.setCategory(category);

        return mapToDto(subCategoryRepo.save(subCategory));
    }

    @Transactional
    public Optional<SubCategoryDto> patchSubCategory(Long id, SubCategoryDto updates) {
        return subCategoryRepo.findById(id).map(existing -> {
            if (updates.getSubCategoryName() != null) {
                existing.setName(updates.getSubCategoryName());
                existing.setSlug(generateSlug(updates.getSubCategoryName()));
            }
            if (updates.getSubCategoryImageUrl() != null) existing.setImageUrl(updates.getSubCategoryImageUrl());
            if (updates.getSlugSubCategoryName() != null) existing.setSlug(updates.getSlugSubCategoryName());

            if (updates.getCategoryId() != null) {
                categoryRepo.findById(updates.getCategoryId()).ifPresent(existing::setCategory);
            }
            return mapToDto(subCategoryRepo.save(existing));
        });
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryBySubCategoryNameDto(String subCategoryName) {
        return subCategoryRepo.findByNameIgnoreCase(subCategoryName)
                .map(SubCategory::getCategory)
                .map(cat -> {
                    CategoryDto dto = new CategoryDto();
                    dto.setId(cat.getId());
                    dto.setName(cat.getName());
                    dto.setCategoryImageUrl(cat.getCategoryImageUrl());
                    dto.setSlug(cat.getSlug());
                    return dto;
                });
    }

    @Transactional(readOnly = true)
    public List<SubCategoryDto> getSubCategoryByCategoryNameDto(String categoryName) {
        return subCategoryRepo.findByCategory_Name(categoryName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubCategoryDto> getSubCategoryByCategoryIdDto(Long categoryId) {
        return subCategoryRepo.findByCategory_Id(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}