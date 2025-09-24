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

    @GetMapping
    public List<Brands> getAllBrands() {
        return brandsRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Brands> getBrandById(@PathVariable Long id) {
        return brandsRepo.findById(id);
    }
    @GetMapping("/brand/by-name/{brandName}")
    public Optional<Brands> getBrandByName(@PathVariable String brandName) {
        return brandService.getBrandByName(brandName);
          //      .map(ResponseEntity::ok)
            //    .orElse(ResponseEntity.notFound().build());
    }
}




