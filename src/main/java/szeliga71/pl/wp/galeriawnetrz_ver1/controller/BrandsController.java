package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.BrandDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.BrandService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandsController {

    private final BrandsRepo brandsRepo;

    private final BrandService brandService;

    @Autowired
    public BrandsController(BrandsRepo brandsRepo, BrandService brandService) {
        this.brandsRepo = brandsRepo;
        this.brandService = brandService;
    }


    // 🔹 GET po ID
    @GetMapping("/id/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 GET wszystkie
    @GetMapping("/all")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }
    @GetMapping("/brand/by-name/{brandName}")
    public ResponseEntity<BrandDto> getBrandByName(@PathVariable String brandName) {
        return brandService.getBrandByName(brandName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}




