package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepo categoryRepo;

    // --- NOWE METODY DLA KONTROLERA ---

    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryByName(String name) {
        // Ta metoda wymaga istnienia findByNameIgnoreCase w CategoryRepo
        return categoryRepo.findByNameIgnoreCase(name)
                .map(this::mapToDto);
    }

    // --- ISTNIEJĄCE METODY ---

    public CategoryDto saveCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setCategoryImageUrl(dto.getCategoryImageUrl());
        category.setSlug(generateSlug(dto.getName()));
        return mapToDto(categoryRepo.save(category));
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setName(dto.getName());
        category.setCategoryImageUrl(dto.getCategoryImageUrl());
        category.setSlug(generateSlug(dto.getName()));

        return mapToDto(categoryRepo.save(category));
    }

    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- POMOCNICZE ---

    private CategoryDto mapToDto(Category category) {
        if (category == null) return null;
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCategoryImageUrl(category.getCategoryImageUrl());
        dto.setSlug(category.getSlug());
        return dto;
    }

    private String generateSlug(String name) {
        if (name == null) return "";
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}