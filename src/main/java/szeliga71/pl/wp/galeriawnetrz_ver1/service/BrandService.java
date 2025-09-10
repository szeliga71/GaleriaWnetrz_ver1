package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.BrandDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    BrandsRepo brandsRepo;

    public List<BrandDto> getAllBrands() {
        return brandsRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public Optional<BrandDto> getBrandById(Long id) {
        return brandsRepo.findById(id)
                .map(this::mapToDto);
    }

    public BrandDto createBrand(BrandDto brandDto) {
        Brands brand = mapToEntity(brandDto);
        brand.setSlugName(generateSlug(brandDto.getBrandName()));

        Brands saved = brandsRepo.save(brand);
        return mapToDto(saved);
    }

    private BrandDto mapToDto(Brands brand) {
        BrandDto dto = new BrandDto();
        dto.setBrandId(brand.getBrandId());
        dto.setBrandName(brand.getBrandName());
        dto.setBrandImageUrl(brand.getBrandImageUrl());
        dto.setBrandUrl(brand.getBrandUrl());
        dto.setSlugName(brand.getSlugName());
        dto.setBrandDescriptionPL(splitText(brand.getBrandDescriptionPL(), 100));
        dto.setBrandDescriptionENG(splitText(brand.getBrandDescriptionENG(), 100));

        return dto;
    }

    private Brands mapToEntity(BrandDto dto) {
        Brands brand = new Brands();
        brand.setBrandName(dto.getBrandName());
        brand.setBrandImageUrl(dto.getBrandImageUrl());
        brand.setBrandUrl(dto.getBrandUrl());
        brand.setSlugName(dto.getSlugName());
        dto.setBrandDescriptionENG(splitText(brand.getBrandDescriptionENG(), 100));
        dto.setBrandDescriptionPL(splitText(brand.getBrandDescriptionPL(), 100));
        return brand;
    }

    private List<String> splitText(String text, int size) {
        if (text == null) return null;
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            parts.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return parts;
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // zamień spacje/znaki na "-"
                .replaceAll("^-|-$", "");       // usuń "-" z początku/końca
    }
    public BrandDto saveBrand(BrandDto dto) {
        Brands brand = mapToEntity(dto);
        Brands saved = brandsRepo.save(brand);
        return mapToDto(saved);
    }




}
