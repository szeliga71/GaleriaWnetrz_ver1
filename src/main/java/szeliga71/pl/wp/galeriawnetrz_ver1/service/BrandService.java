package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.BrandCreateDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.BrandDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrandService {

    @Autowired
    BrandsRepo brandsRepo;



    public Optional<BrandDto> getBrandById(Long id) {
        return brandsRepo.findById(id)
                .map(this::mapToDto);
    }


    public List<BrandDto> getAllBrands() {
        return brandsRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteBrand(Long id) {
        if (brandsRepo.existsById(id)) {
            brandsRepo.deleteById(id);
        }
    }
    @Transactional
    public void deleteBrandByName(String brandName) {
        Brands existing = brandsRepo.findByBrandNameIgnoreCase(brandName)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        brandsRepo.delete(existing);
    }
    public void deleteAllAndReset() {
        brandsRepo.truncateBrands();
    }

    @Transactional
    public Optional<BrandDto> getBrandByName(String brandName) {
        return brandsRepo.findByBrandNameIgnoreCase(brandName)
        .map(this::mapToDto);
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

    @Transactional
    public Optional<BrandDto> patchBrand(Long id, BrandCreateDto updates) {
        return brandsRepo.findById(id).map(existing -> {
            if (updates.getBrandName() != null) existing.setBrandName(updates.getBrandName());
            if (updates.getBrandUrl() != null) existing.setBrandUrl(updates.getBrandUrl());
            if (updates.getBrandDescriptionENG() != null)
                existing.setBrandDescriptionENG(String.join("", updates.getBrandDescriptionENG()));
            if (updates.getBrandDescriptionPL() != null)
                existing.setBrandDescriptionPL(String.join("", updates.getBrandDescriptionPL()));
            if (updates.getBrandImageUrl() != null) existing.setBrandImageUrl(updates.getBrandImageUrl());
            if (updates.getSlugName() != null) existing.setSlugName(updates.getSlugName());

            Brands saved = brandsRepo.save(existing);
            return mapToDto(saved);
        });
    }
    @Transactional
    public BrandDto updateBrand(Long id, BrandCreateDto dto) {
        Brands existing = brandsRepo.findById(id)
                .orElse(null);

        existing.setBrandName(dto.getBrandName());
        existing.setBrandUrl(dto.getBrandUrl());
        existing.setBrandImageUrl(dto.getBrandImageUrl());
        existing.setBrandDescriptionENG(dto.getBrandDescriptionENG() != null ? String.join("", dto.getBrandDescriptionENG()) : null);
        existing.setBrandDescriptionPL(dto.getBrandDescriptionPL() != null ? String.join("", dto.getBrandDescriptionPL()) : null);
        existing.setSlugName(dto.getSlugName());

        Brands saved = brandsRepo.save(existing);
        return mapToDto(saved);
    }




    @Transactional
    public BrandDto updateBrandByName(String brandName, BrandCreateDto dto) {
        Brands existing = brandsRepo.findByBrandNameIgnoreCase(brandName)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        if (dto.getBrandName() != null) existing.setBrandName(dto.getBrandName());
        if (dto.getBrandUrl() != null) existing.setBrandUrl(dto.getBrandUrl());
        if (dto.getBrandImageUrl() != null) existing.setBrandImageUrl(dto.getBrandImageUrl());
        if (dto.getBrandDescriptionENG() != null) existing.setBrandDescriptionENG(String.join("", dto.getBrandDescriptionENG()));
        if (dto.getBrandDescriptionPL() != null) existing.setBrandDescriptionPL(String.join("", dto.getBrandDescriptionPL()));
        if(dto.getSlugName()!=null) existing.setSlugName(dto.getSlugName());

        Brands saved = brandsRepo.save(existing);
        return mapToDto(saved);
    }

    @Transactional
    public Optional<BrandDto> patchBrandByName(String brandName, BrandCreateDto updates) {
        return brandsRepo.findByBrandNameIgnoreCase(brandName).map(existing -> {
            if (updates.getBrandName() != null) existing.setBrandName(updates.getBrandName());
            if (updates.getBrandUrl() != null) existing.setBrandUrl(updates.getBrandUrl());
            if (updates.getBrandImageUrl() != null) existing.setBrandImageUrl(updates.getBrandImageUrl());
            if (updates.getBrandDescriptionENG() != null)
                existing.setBrandDescriptionENG(String.join("", updates.getBrandDescriptionENG()));
            if (updates.getBrandDescriptionPL() != null)
                existing.setBrandDescriptionPL(String.join("", updates.getBrandDescriptionPL()));
            if (updates.getSlugName() != null) existing.setSlugName(updates.getSlugName());

            Brands saved = brandsRepo.save(existing);
            return mapToDto(saved);
        });
    }
    @Transactional
    public BrandDto createBrandFromDto(BrandCreateDto dto) {
        Brands brand = new Brands();
        brand.setBrandName(dto.getBrandName());
        brand.setBrandImageUrl(dto.getBrandImageUrl());
        brand.setBrandUrl(dto.getBrandUrl());
        brand.setBrandDescriptionPL(dto.getBrandDescriptionPL());
        brand.setBrandDescriptionENG(dto.getBrandDescriptionENG());
        brand.setSlugName(generateSlug(dto.getBrandName()));

        Brands saved = brandsRepo.save(brand);
        return mapToDto(saved); // tutaj splitText zmieni string na List<String>
    }

    @Transactional
    public Optional<BrandDto> getBrandBySlugName(String slugBrandName) {
        return brandsRepo.findBySlugNameIgnoreCase(slugBrandName)
                .map(this::mapToDto);

    }
}
