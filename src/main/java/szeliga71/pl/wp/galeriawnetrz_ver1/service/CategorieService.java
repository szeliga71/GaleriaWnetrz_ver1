package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;

import java.util.List;

import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoriesDto;


@Service
@Transactional
public class CategorieService {

    private final CategoriesRepo categoriesRepo;

    public CategorieService(CategoriesRepo categoriesRepo) {
        this.categoriesRepo = categoriesRepo;
    }

    public List<CategoriesDto> getAllCategories() {
        return categoriesRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public Categories getCategoryById(Long id) {
        return categoriesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoriesDto saveCategory(CategoriesDto dto) {
        Categories category = new Categories();
        category.setCategoryName(dto.getCategoryName());
        category.setCategoryImageUrl(dto.getCategoryImageUrl());
        category.setSlugCategoryName(generateSlug(dto.getCategoryName()));

        Categories saved = categoriesRepo.save(category);
        return mapToDto(saved);
    }

    public CategoriesDto updateCategory(Long id, CategoriesDto dto) {
        Categories category = getCategoryById(id);
        category.setCategoryName(dto.getCategoryName());
        category.setCategoryImageUrl(dto.getCategoryImageUrl());
        category.setSlugCategoryName(generateSlug(dto.getCategoryName()));

        Categories saved = categoriesRepo.save(category);
        return mapToDto(saved);
    }

    public void deleteCategory(Long id) {
        categoriesRepo.deleteById(id);
    }

    public void deleteAllCategories() {
        categoriesRepo.deleteAll();
    }

    public CategoriesDto mapToDto(Categories category) {
        CategoriesDto dto = new CategoriesDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setCategoryImageUrl(category.getCategoryImageUrl());
        dto.setSlugCategoryName(category.getSlugCategoryName());

        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            dto.setSubCategories(
                    category.getSubCategories().stream()
                            .map(sc -> {
                                SubCategoriesDto scDto = new SubCategoriesDto();
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
}
