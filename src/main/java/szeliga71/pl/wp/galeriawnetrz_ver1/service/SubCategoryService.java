package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CreateSubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;

import java.util.List;
import java.util.Optional;

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
                .orElse(null);
    }


    public SubCategoryDto saveSubCategory(SubCategoryDto dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElse(null);

        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(dto.getSubCategoryName());
        subCategory.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlugSubCategoryName(generateSlug(dto.getSubCategoryName()));
        subCategory.setCategory(category);

        SubCategory saved = subCategoryRepo.save(subCategory);
        return mapToDto(saved);
    }


    public SubCategoryDto updateSubCategory(Long id, SubCategoryDto dto) {
        SubCategory subCategory = getSubCategoryById(id);
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElse(null);

        subCategory.setSubCategoryName(dto.getSubCategoryName());
        subCategory.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        subCategory.setSlugSubCategoryName(dto.getSlugSubCategoryName());
       // subCategory.setSlugSubCategoryName(generateSlug(dto.getSubCategoryName()));
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

    @Transactional
    public void deleteSubCategoryBySubCategoryName(String subCategoryName) {
        SubCategory existing=subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName)
                .orElse(null);
        Category parent = existing.getCategory();
        if (parent != null) {
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
    public Optional<SubCategoryDto> patchSubCategory(Long id, SubCategoryDto updates) {
        return subCategoryRepo.findById(id).map(existing -> {
            if (updates.getSubCategoryName() != null) existing.setSubCategoryName(updates.getSubCategoryName());
            if (updates.getSubCategoryImageUrl() != null) existing.setSubCategoryImageUrl(updates.getSubCategoryImageUrl());
            existing.setSlugSubCategoryName(updates.getSlugSubCategoryName());
            if(updates.getCategoryId() != null) {
                Optional<Category> parent = categoryRepo.findById(updates.getCategoryId());
                existing.setCategory(parent.orElse(null));
            }

            SubCategory saved = subCategoryRepo.save(existing);
            return mapToDto(saved);
        });
    }

   /* @Transactional
    public SubCategoryDto updateSubCategoryByName(String subCategoryName, SubCategoryDto dto) {
        SubCategory existing = subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName)
                .orElse(null);

        if(dto.getSubCategoryName() !=null ) if (existing != null) {
            existing.setSubCategoryName(dto.getSubCategoryName());
        }
        if(dto.getSubCategoryImageUrl()!=null) if (existing != null) {
            existing.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        }
        if (existing != null) {
            existing.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        }
        SubCategory saved = null;
        if (existing != null) {
            saved = subCategoryRepo.save(existing);
        }
        if (saved != null) {
            return mapToDto(saved);
        }
    }*/

    @Transactional
    public SubCategoryDto updateSubCategoryByName(String subCategoryName, SubCategoryDto dto) {
        SubCategory existing = subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName)
                .orElse(null);

        if (dto.getSubCategoryName() != null) {
            existing.setSubCategoryName(dto.getSubCategoryName());
        }
        if (dto.getSubCategoryImageUrl() != null) {
            existing.setSubCategoryImageUrl(dto.getSubCategoryImageUrl());
        }
        if (dto.getSlugSubCategoryName() != null) {
            existing.setSlugSubCategoryName(dto.getSlugSubCategoryName());
        }
        if(dto.getCategoryId() != null) {
            Optional<Category> parent = categoryRepo.findById(dto.getCategoryId());
            existing.setCategory(parent.orElse(null));
        }

        SubCategory saved = subCategoryRepo.save(existing);
        return mapToDto(saved);
    }

    @Transactional
    public Optional<SubCategoryDto> patchSubCategoryByName(String subCategoryName, SubCategoryDto updates) {
        return subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName).map(existing -> {
            if (updates.getSubCategoryName() != null) existing.setSubCategoryName(updates.getSubCategoryName());
            if (updates.getSubCategoryImageUrl() != null) existing.setSubCategoryImageUrl(updates.getSubCategoryImageUrl());
            if (updates.getCategoryId() != null) {
                Category category = categoryRepo.findById(updates.getCategoryId())
                        .orElse(null);
                existing.setCategory(category);
            }
            existing.setSlugSubCategoryName(updates.getSlugSubCategoryName());
            SubCategory saved = subCategoryRepo.save(existing);
            return mapToDto(saved);
        });

    }

    public Optional<SubCategory> getSubCategoryByName(String subCategoryName) {
        return subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName);
    }

}



