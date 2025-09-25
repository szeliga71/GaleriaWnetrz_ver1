package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Optional<Category> getCategoryByName(String categoryName){
        return categoryRepo.findByCategoryNameIgnoreCase(categoryName);
    }


    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public Category getCategoryById(Long id) {
        //return categoriesRepo.findById(id)
        return categoryRepo.findById(id).orElse(null);
                //.orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryDto saveCategory(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setCategoryImageUrl(dto.getCategoryImageUrl());
        category.setSlugCategoryName(generateSlug(dto.getCategoryName()));

        Category saved = categoryRepo.save(category);
        return mapToDto(saved);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = getCategoryById(id);
        category.setCategoryName(dto.getCategoryName());
        category.setCategoryImageUrl(dto.getCategoryImageUrl());
        category.setSlugCategoryName(generateSlug(dto.getCategoryName()));

        Category saved = categoryRepo.save(category);
        return mapToDto(saved);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    public void deleteAllAndReset() {
        categoryRepo.truncateCategories();
    }

    public CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setCategoryImageUrl(category.getCategoryImageUrl());
        dto.setSlugCategoryName(category.getSlugCategoryName());

        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            dto.setSubCategories(
                    category.getSubCategories().stream()
                            .map(sc -> {
                                SubCategoryDto scDto = new SubCategoryDto();
                                scDto.setSubCategoryId(sc.getSubCategoryId());
                                scDto.setSubCategoryName(sc.getSubCategoryName());
                                scDto.setSubCategoryImageUrl(sc.getSubCategoryImageUrl());
                                scDto.setSlugSubCategoryName(sc.getSlugSubCategoryName());
                                scDto.setCategoryId(sc.getCategory().getCategoryId()); // tylko ID
                                return scDto;
                            })
                            .toList()
            );
        }

        return dto;
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    @Transactional
    public Optional<CategoryDto> patchCategory(Long id, CategoryDto updates) {
        return categoryRepo.findById(id).map(existing -> {
            if (updates.getCategoryName() != null) existing.setCategoryName(updates.getCategoryName());
            if (updates.getCategoryImageUrl() != null) existing.setCategoryImageUrl(updates.getCategoryImageUrl());
            if(updates.getSlugCategoryName() !=null) existing.setSlugCategoryName(updates.getSlugCategoryName());
            Category saved = categoryRepo.save(existing);
            return mapToDto(saved);
        });
    }

    @Transactional
    public CategoryDto updateCategoryByName(String categoryName, CategoryDto dto) {
        Category existing = categoryRepo.findByCategoryNameIgnoreCase(categoryName)
                .orElse(null);

        if(dto.getCategoryName() !=null ) if (existing != null) {
            existing.setCategoryName(dto.getCategoryName());
        }
        if(dto.getCategoryImageUrl()!=null) if (existing != null) {
            existing.setCategoryImageUrl(dto.getCategoryImageUrl());
        }
        if (existing != null) {
            existing.setSlugCategoryName(dto.getSlugCategoryName());
        }
        Category saved = null;
        if (existing != null) {
            saved = categoryRepo.save(existing);
        }
        return mapToDto(saved);
    }
    @Transactional
    public Optional<CategoryDto> patchCategoryByName(String categoryName, CategoryDto updates) {
        return categoryRepo.findByCategoryNameIgnoreCase(categoryName).map(existing -> {
            if (updates.getCategoryName() != null) existing.setCategoryName(updates.getCategoryName());
            if (updates.getCategoryImageUrl() != null) existing.setCategoryImageUrl(updates.getCategoryImageUrl());
            if(updates.getSlugCategoryName() !=null) existing.setSlugCategoryName(updates.getSlugCategoryName());
            Category saved = categoryRepo.save(existing);
            return mapToDto(saved);
        });

    }

@Transactional
    public void deleteCategoryByCategoryName(String categoryName) {
        Category existing = categoryRepo.findByCategoryNameIgnoreCase(categoryName)
                .orElse(null);
    if (existing != null) {
        categoryRepo.delete(existing);
    }
}


}
