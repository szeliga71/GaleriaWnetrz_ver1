package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandsController {

    private final BrandsRepo brandsRepo;

    @Autowired
    public BrandsController(BrandsRepo brandsRepo) {
        this.brandsRepo = brandsRepo;
    }

    @GetMapping
    public List<Brands> getAllBrands() {
        return brandsRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Brands> getBrandById(@PathVariable Long id) {
        return brandsRepo.findById(id);
    }
}



