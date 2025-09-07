package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductsRepo;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductsRepo productsRepo;

    public ProductService(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    public List<Products> getAllProducts() {
        return productsRepo.findAll();
    }

    public Optional<Products> getProductsByCategory(Long categoryId) {
        return productsRepo.findByCategoryId(categoryId);
    }
    public Optional<Products>getProductsByBrand(Long brandId) {
        return productsRepo.findByBrandId(brandId);
    }

}
