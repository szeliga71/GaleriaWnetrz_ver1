package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;


import java.util.List;

@Service
public class CategorieService {

    @Autowired
    CategoriesRepo categoriesRepo;

    public List<Categories> getAllCategories() {
        return categoriesRepo.findAll();
    }

    public Categories getCategoryById(Long id) {
        return categoriesRepo.findById(id).get();
    }

    public CategoriesDto createCategorie(CategoriesDto categoriesDto) {
        Categories categories = mapToEntity(categoriesDto);
        categories.setSlugCategoryName(generateSlug(categoriesDto.getCategoryName()));

        Categories saved = categoriesRepo.save(categories);
        return mapToDto(saved);
    }
    public CategoriesDto saveCategory(CategoriesDto dto) {
        Categories category = mapToEntity(dto);
        Categories savedCategory = categoriesRepo.save(category);
        return mapToDto(savedCategory);
    }

    private CategoriesDto mapToDto(Categories categories) {
        CategoriesDto dto = new CategoriesDto();
        dto.setCategoryId(categories.getCategoryId());
        dto.setCategoryName(categories.getCategoryName());
        dto.setCategoryImageUrl(categories.getCategoryImageUrl());
        dto.setSlugCategoryName(categories.getSlugCategoryName());
        return dto;
    }

    private Categories mapToEntity(CategoriesDto dto) {
        Categories categories = new Categories();
        categories.setCategoryName(dto.getCategoryName());
        categories.setCategoryImageUrl(dto.getCategoryImageUrl());
        categories.setSlugCategoryName(dto.getSlugCategoryName());

        return categories;
    }
    private String generateSlug(String name) {
        if (name == null) return null;
        return name
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // zamień spacje/znaki na "-"
                .replaceAll("^-|-$", "");       // usuń "-" z początku/końca
    }
}
