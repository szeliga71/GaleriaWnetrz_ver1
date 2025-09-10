package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductsRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductsRepo productsRepo;

    public ProductService(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    public List<ProductDto> getAllProducts() {
        return productsRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public Optional<ProductDto> getProductById(Long productId) {
        return productsRepo.findById(productId)
                .map(this::mapToDto);
    }

    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productsRepo.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByBrand(Long brandId) {
        return productsRepo.findByBrandId(brandId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsBySubCategory(Long subCategoryId) {
        return productsRepo.findBySubCategoryId(subCategoryId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private List<String> splitText(String text, int size) {
        if (text == null) return null;
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            parts.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return parts;
    }

    public ProductDto saveProduct(ProductDto dto) {
        Products product = mapToEntity(dto);
        Products saved = productsRepo.save(product);
        return mapToDto(saved);
    }


    private ProductDto mapToDto(Products product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setBrandId(product.getBrandId());
        dto.setImages(product.getImages());
        dto.setDescriptionENG(product.getDescriptionENG() != null ? List.of(product.getDescriptionENG()) : null);
        dto.setDescriptionPL(product.getDescriptionPL() != null ? List.of(product.getDescriptionPL()) : null);
        dto.setCategoryId(product.getCategoryId());
        dto.setSubCategoryId(product.getSubCategoryId());
        return dto;
    }


    private Products mapToEntity(ProductDto dto) {
        Products product = new Products();
        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setBrandId(dto.getBrandId());
        product.setImages(dto.getImages());
        product.setDescriptionENG(dto.getDescriptionENG() != null ? String.join("", dto.getDescriptionENG()) : null);
        product.setDescriptionPL(dto.getDescriptionPL() != null ? String.join("", dto.getDescriptionPL()) : null);
        product.setCategoryId(dto.getCategoryId());
        product.setSubCategoryId(dto.getSubCategoryId());
        return product;
    }



}





