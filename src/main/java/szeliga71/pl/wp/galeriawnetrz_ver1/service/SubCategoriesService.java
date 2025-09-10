package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoriesDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoriesRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoriesService {

    @Autowired
    private SubCategoriesRepo subCategoriesRepo;

    public SubCategoriesService(SubCategoriesRepo subCategoriesRepo) {
        this.subCategoriesRepo = subCategoriesRepo;
    }

    public SubCategoriesDto saveSubCategory(SubCategoriesDto dto) {
        SubCategories subCategory = mapToEntity(dto);
        SubCategories saved = subCategoriesRepo.save(subCategory);
        return mapToDto(saved);
    }

    public List<SubCategoriesDto> getAllSubCategories() {
        return subCategoriesRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubCategoriesDto mapToDto(SubCategories entity) {
        SubCategoriesDto dto = new SubCategoriesDto();
        dto.setSubCategoryId(entity.getSubCategoryId());
        dto.setSubCategoryName(entity.getSubCategoryName());
        dto.setSubCategoryImageUrl(entity.getSubCategoryImageUrl());
        dto.setSlugSubCategoryName(entity.getSlugSubCategoryName());
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
