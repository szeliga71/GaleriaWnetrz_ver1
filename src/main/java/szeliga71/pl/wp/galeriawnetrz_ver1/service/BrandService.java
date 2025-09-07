package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandsRepo brandsRepo;

    public List<Brands> getAllBrands() {
        return brandsRepo.findAll();
    }

    public Brands getBrandById(Long id) {
        return brandsRepo.findById(id).get();
    }
}
