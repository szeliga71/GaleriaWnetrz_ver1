package szeliga71.pl.wp.galeriawnetrz_ver1.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;

import szeliga71.pl.wp.galeriawnetrz_ver1.dto.SubCategoryDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;

import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;


import java.util.*;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private BrandsRepo brandsRepo;
    @Autowired
    private SubCategoryRepo subCategoryRepo;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SubCategoryService subCategoryService;

    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(this::mapToDto).toList();
    }

    public Optional<ProductDto> getProductById(Long productId) {
        return productRepo.findById(productId).map(this::mapToDto);
    }


    public List<ProductDto> getProductsByCategoryName(String categoryName) {
        return productRepo.findByCategoryNameIgnoreCase(categoryName).stream().map(this::mapToDto).toList();
    }

    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productRepo.findBySubCategoryNameIgnoreCase(subCategoryName).stream().map(this::mapToDto).toList();
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public void deleteProductByName(String productName) {
        Product existing = productRepo.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.delete(existing);
    }

    public void deleteAllAndReset() {
        productRepo.truncateProducts();
    }

    private List<String> splitText(String text, int size) {
        if (text == null) return null;
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            parts.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return parts;
    }


    //===================================================================================
    public ProductDto saveProduct(ProductDto dto) {
        Product product = dto.getProductId() != null
                ? productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()))
                : new Product();

        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());

        product.setDescriptionENG(dto.getDescriptionENG() != null
                ? dto.getDescriptionENG().stream()
                .map(s -> s.replace("\n", "\\n").replace("\r", "\\r"))
                .reduce("", String::concat)
                : null);

        product.setDescriptionPL(dto.getDescriptionPL() != null
                ? dto.getDescriptionPL().stream()
                .map(s -> s.replace("\n", "\\n").replace("\r", "\\r"))
                .reduce("", String::concat)
                : null);

        if (dto.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName());
        if (dto.getSubCategoryName() != null) subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName());
        if (dto.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName());

        return mapToDto(productRepo.save(product));
    }

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setImages(product.getImages());

        // Przy odczycie konwertujemy \\n i \\r z powrotem na normalne znaki nowej linii
        dto.setDescriptionENG(splitText(
                product.getDescriptionENG() != null
                        ? product.getDescriptionENG().replace("\\n", "\n").replace("\\r", "\r")
                        : null, 100));

        dto.setDescriptionPL(splitText(
                product.getDescriptionPL() != null
                        ? product.getDescriptionPL().replace("\\n", "\n").replace("\\r", "\r")
                        : null, 100));

        if (product.getCategoryName() != null) dto.setCategoryName(product.getCategoryName());
        if (product.getSubCategoryName() != null) dto.setSubCategoryName(product.getSubCategoryName());
        if (product.getBrandName() != null) {
            dto.setBrandName(product.getBrandName());
            dto.setBrandName(product.getBrandName());
        }
        return dto;
    }


    private Product mapToEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());

        product.setDescriptionENG(dto.getDescriptionENG() != null
                ? dto.getDescriptionENG().stream()
                .map(s -> s.replace("\n", "\\n").replace("\r", "\\r"))
                .reduce("", String::concat)
                : null);

        product.setDescriptionPL(dto.getDescriptionPL() != null
                ? dto.getDescriptionPL().stream()
                .map(s -> s.replace("\n", "\\n").replace("\r", "\\r"))
                .reduce("", String::concat)
                : null);

        if (dto.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName());
        if (dto.getSubCategoryName() != null) subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName());
        if (dto.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName());

        return product;
    }


    // ------------------- NOWE METODY UPDATE / PATCH -------------------

    public ProductDto updateProductByName(String productName, ProductDto dto) {
        Product product = productRepo.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());
        product.setDescriptionENG(dto.getDescriptionENG() != null ? String.join("", dto.getDescriptionENG()) : null);
        product.setDescriptionPL(dto.getDescriptionPL() != null ? String.join("", dto.getDescriptionPL()) : null);

        if (dto.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName());
        if (dto.getSubCategoryName() != null) subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName());
        if (dto.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName());

        return mapToDto(productRepo.save(product));
    }

    public Optional<ProductDto> patchProductByName(String productName, ProductDto updates) {
        return productRepo.findByNameIgnoreCase(productName).map(product -> {
            if (updates.getName() != null) product.setName(updates.getName());
            if (updates.getPdfUrl() != null) product.setPdfUrl(updates.getPdfUrl());
            if (updates.getImages() != null) product.setImages(updates.getImages());
            if (updates.getDescriptionENG() != null) product.setDescriptionENG(String.join("", updates.getDescriptionENG()));
            if (updates.getDescriptionPL() != null) product.setDescriptionPL(String.join("", updates.getDescriptionPL()));

            if (updates.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(updates.getCategoryName());
            if (updates.getSubCategoryName() != null) subCategoryRepo.findBySubCategoryNameIgnoreCase(updates.getSubCategoryName());
            if (updates.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(updates.getBrandName());

            return mapToDto(productRepo.save(product));
        });
    }
    public Optional<ProductDto> patchProduct(Long id, ProductDto updates) {
        return productRepo.findById(id).map(existing -> {
            if (updates.getName() != null) existing.setName(updates.getName());
            if (updates.getPdfUrl() != null) existing.setPdfUrl(updates.getPdfUrl());
                    if (updates.getImages() != null) existing.setImages(updates.getImages());
                    if (updates.getDescriptionENG() != null)existing.setDescriptionENG(String.join("", updates.getDescriptionENG()));
                    if (updates.getDescriptionPL() != null) existing.setDescriptionPL(String.join("", updates.getDescriptionPL()));
                    if (updates.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(updates.getCategoryName());
                    if (updates.getSubCategoryName() != null) subCategoryRepo.findBySubCategoryNameIgnoreCase(updates.getSubCategoryName());
                    if (updates.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(updates.getBrandName());


            Product saved = productRepo.save(existing);
            return mapToDto(saved);
        });
    }



    public Optional<ProductDto> updateProduct(Long id,ProductDto dto) {

        return productRepo.findById(id).map(existing -> {
            if (dto.getName() != null) existing.setName(existing.getName());
            if (dto.getPdfUrl() != null) existing.setPdfUrl(existing.getPdfUrl());
            if (dto.getImages() != null) existing.setImages(existing.getImages());
            if (dto.getDescriptionENG() != null)existing.setDescriptionENG(String.join("", existing.getDescriptionENG()));
            if (dto.getDescriptionPL() != null) existing.setDescriptionPL(String.join("", existing.getDescriptionPL()));
            if (dto.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(existing.getCategoryName());
            if (dto.getSubCategoryName() != null) subCategoryRepo.findBySubCategoryNameIgnoreCase(existing.getSubCategoryName());
            if (dto.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(existing.getBrandName());


            Product saved = productRepo.save(existing);
            return mapToDto(saved);
        });
    }


    private Long findOrCreateBrandByBrandName(String brandname) {
        if (brandname == null || brandname.isBlank()) return null;

        return brandsRepo.findAll().stream()
                .filter(c -> c.getBrandName().equalsIgnoreCase(brandname.trim()))
                .findFirst()
                .map(c -> c.getBrandId())
                .orElseGet(() -> {
                    // Tworzymy nową kategorię
                    var newCat = new szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands();
                    newCat.setBrandName(brandname.trim());
                    brandsRepo.save(newCat);
                    return newCat.getBrandId();
                });
    }


    private Long findOrCreateCategoryByName(String name) {
        if (name == null || name.isBlank()) return null;

        return categoryRepo.findAll().stream()
                .filter(c -> c.getCategoryName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .map(c -> c.getCategoryId())
                .orElseGet(() -> {
                    // Tworzymy nową kategorię
                    var newCat = new Category();
                    newCat.setCategoryName(name.trim());
                    categoryRepo.save(newCat);
                    return newCat.getCategoryId();
                });
    }

    private Long findOrCreateSubCategoryByName(String name, Long categoryId) {
        if (name == null || name.isBlank() || categoryId == null) return null;

        return subCategoryRepo.findAll().stream()
                .filter(sc -> sc.getSubCategoryName().equalsIgnoreCase(name.trim()) &&
                        sc.getCategory() != null &&
                        Objects.equals(sc.getCategory().getCategoryId(), categoryId))
                .findFirst()
                .map(sc -> sc.getSubCategoryId())
                .orElseGet(() -> {
                    // Tworzymy nową subkategorię
                    var cat = categoryRepo.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found for subcategory"));
                    var newSub = new SubCategory();
                    newSub.setSubCategoryName(name.trim());
                    newSub.setCategory(cat);
                    subCategoryRepo.save(newSub);
                    return newSub.getSubCategoryId();
                });
    }


    public List<ProductDto> getProductByName(String productName) {
        return productRepo.findByNameIgnoreCase(productName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByCategoryNameAndSubCategoryName(String categoryName, String subCategoryName) {
        return productRepo.findByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(categoryName, subCategoryName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByBrandName(String brandName) {
        return productRepo.findByBrandNameIgnoreCase(brandName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

}




