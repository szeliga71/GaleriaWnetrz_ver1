package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;
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


    @Cacheable(value = "products", key = "'allProducts'")
    /*public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(this::mapToDto).toList();
    }*/
    public List<ProductDto> getAllProducts(Integer page, Integer size) {
        Pageable pageable = page != null && size != null ? PageRequest.of(page, size) : Pageable.unpaged();
        Page<Product> products = productRepo.findAll(pageable);
        return products.stream().map(this::mapToDto).toList();
    }

    public Optional<ProductDto> getProductById(Long productId) {
        return productRepo.findById(productId).map(this::mapToDto);
    }

    @Cacheable(value = "products", key = "#categoryName")
    /*public List<ProductDto> getProductsByCategoryName(String categoryName) {
        return productRepo.findByCategoryNameIgnoreCase(categoryName).stream().map(this::mapToDto).toList();
    }*/
    public List<ProductDto> getProductsByCategoryName(String categoryName, Integer page, Integer size) {
        Pageable pageable = page != null && size != null ? PageRequest.of(page, size) : Pageable.unpaged();
        Page<Product> products = productRepo.findByCategoryNameIgnoreCase(categoryName, pageable);
        return products.stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "products", key = "#subCategoryName")
    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productRepo.findBySubCategoryNameIgnoreCase(subCategoryName).stream().map(this::mapToDto).toList();
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProductByName(String productName) {
        Product existing = productRepo.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.delete(existing);
    }

    @CacheEvict(value = "products", allEntries = true)
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

    @CacheEvict(value = "products", allEntries = true)
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



    // ------------------- METODY UPDATE / PATCH -------------------


    @CachePut(value = "products", key = "#productName")
    @CacheEvict(value = "products", key = "'allProducts'")
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

    @CachePut(value = "products", key = "#productName")
    @CacheEvict(value = "products", key = "'allProducts'")
    public Optional<ProductDto> patchProductByName(String productName, ProductDto updates) {
        return productRepo.findByNameIgnoreCase(productName).map(product -> {
            if (updates.getName() != null) product.setName(updates.getName());
            if (updates.getPdfUrl() != null) product.setPdfUrl(updates.getPdfUrl());
            if (updates.getImages() != null) product.setImages(updates.getImages());
            if (updates.getDescriptionENG() != null)
                product.setDescriptionENG(String.join("", updates.getDescriptionENG()));
            if (updates.getDescriptionPL() != null)
                product.setDescriptionPL(String.join("", updates.getDescriptionPL()));

            if (updates.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(updates.getCategoryName());
            if (updates.getSubCategoryName() != null)
                subCategoryRepo.findBySubCategoryNameIgnoreCase(updates.getSubCategoryName());
            if (updates.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(updates.getBrandName());

            return mapToDto(productRepo.save(product));
        });
    }

    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "products", key = "'allProducts'")
    public Optional<ProductDto> patchProduct(Long id, ProductDto updates) {
        return productRepo.findById(id).map(existing -> {
            if (updates.getName() != null) existing.setName(updates.getName());
            if (updates.getPdfUrl() != null) existing.setPdfUrl(updates.getPdfUrl());
            if (updates.getImages() != null) existing.setImages(updates.getImages());
            if (updates.getDescriptionENG() != null)
                existing.setDescriptionENG(String.join("", updates.getDescriptionENG()));
            if (updates.getDescriptionPL() != null)
                existing.setDescriptionPL(String.join("", updates.getDescriptionPL()));
            if (updates.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(updates.getCategoryName());
            if (updates.getSubCategoryName() != null)
                subCategoryRepo.findBySubCategoryNameIgnoreCase(updates.getSubCategoryName());
            if (updates.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(updates.getBrandName());

            Product saved = productRepo.save(existing);
            return mapToDto(saved);
        });
    }

    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "products", key = "'allProducts'")
    public Optional<ProductDto> updateProduct(Long id, ProductDto dto) {

        return productRepo.findById(id).map(existing -> {
            if (dto.getName() != null) existing.setName(dto.getName());
            if (dto.getPdfUrl() != null) existing.setPdfUrl(dto.getPdfUrl());
            if (dto.getImages() != null) existing.setImages(dto.getImages());
            if (dto.getDescriptionENG() != null) existing.setDescriptionENG(String.join("", dto.getDescriptionENG()));
            if (dto.getDescriptionPL() != null) existing.setDescriptionPL(String.join("", dto.getDescriptionPL()));
            if (dto.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName());
            if (dto.getSubCategoryName() != null)
                subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName());
            if (dto.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName());


            Product saved = productRepo.save(existing);
            return mapToDto(saved);
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

    @Cacheable(value = "products", key = "#brandName")
   /* public List<ProductDto> getProductsByBrandName(String brandName) {
        return productRepo.findByBrandNameIgnoreCase(brandName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }*/public List<ProductDto> getProductsByBrandName(String brandName, Integer page, Integer size) {
        Pageable pageable = page != null && size != null ? PageRequest.of(page, size) : Pageable.unpaged();
        Page<Product> products = productRepo.findByBrandNameIgnoreCase(brandName, pageable);
        return products.stream().map(this::mapToDto).toList();
    }

}

/*
package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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



public Optional<ProductDto> getProductById(Long productId) {
    return productRepo.findById(productId).map(this::mapToDto);
}

// ----------------- PAGINACJA -----------------






public List<ProductDto> getProductsBySubCategoryName(String subCategoryName, Integer page, Integer size) {
    Pageable pageable = page != null && size != null ? PageRequest.of(page, size) : Pageable.unpaged();
    Page<Product> products = productRepo.findBySubCategoryNameIgnoreCase(subCategoryName, pageable);
    return products.stream().map(this::mapToDto).toList();
}



public List<ProductDto> getProductsByCategoryNameAndSubCategoryName(String categoryName, String subCategoryName, Integer page, Integer size) {
    Pageable pageable = page != null && size != null ? PageRequest.of(page, size) : Pageable.unpaged();
    Page<Product> products = productRepo.findByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(categoryName, subCategoryName, pageable);
    return products.stream().map(this::mapToDto).toList();
}

// ----------------- BEZ PAGINACJI -----------------
public List<ProductDto> getProductByName(String productName) {
    return productRepo.findByNameIgnoreCase(productName).stream().map(this::mapToDto).toList();
}

// ----------------- CRUD -----------------
@CacheEvict(value = "products", allEntries = true)
public void deleteProduct(Long id) {
    productRepo.deleteById(id);
}

@CacheEvict(value = "products", allEntries = true)
public void deleteProductByName(String productName) {
    Product existing = productRepo.findByNameIgnoreCase(productName)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    productRepo.delete(existing);
}

@CacheEvict(value = "products", allEntries = true)
public void deleteAllAndReset() {
    productRepo.truncateProducts();
}

@CacheEvict(value = "products", allEntries = true)
public ProductDto saveProduct(ProductDto dto) {
    Product product = dto.getProductId() != null
            ? productRepo.findById(dto.getProductId()).orElse(new Product())
            : new Product();

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

// ----------------- MAPPER -----------------
private ProductDto mapToDto(Product product) {
    ProductDto dto = new ProductDto();
    dto.setProductId(product.getProductId());
    dto.setName(product.getName());
    dto.setPdfUrl(product.getPdfUrl());
    dto.setImages(product.getImages());
    dto.setDescriptionENG(product.getDescriptionENG() != null ? List.of(product.getDescriptionENG()) : null);
    dto.setDescriptionPL(product.getDescriptionPL() != null ? List.of(product.getDescriptionPL()) : null);
    dto.setCategoryName(product.getCategoryName());
    dto.setSubCategoryName(product.getSubCategoryName());
    dto.setBrandName(product.getBrandName());
    return dto;
}

// ------------------- METODY UPDATE / PATCH -------------------


@CachePut(value = "products", key = "#productName")
@CacheEvict(value = "products", key = "'allProducts'")
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

@CachePut(value = "products", key = "#productName")
@CacheEvict(value = "products", key = "'allProducts'")
public Optional<ProductDto> patchProductByName(String productName, ProductDto updates) {
    return productRepo.findByNameIgnoreCase(productName).map(product -> {
        if (updates.getName() != null) product.setName(updates.getName());
        if (updates.getPdfUrl() != null) product.setPdfUrl(updates.getPdfUrl());
        if (updates.getImages() != null) product.setImages(updates.getImages());
        if (updates.getDescriptionENG() != null)
            product.setDescriptionENG(String.join("", updates.getDescriptionENG()));
        if (updates.getDescriptionPL() != null)
            product.setDescriptionPL(String.join("", updates.getDescriptionPL()));

        if (updates.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(updates.getCategoryName());
        if (updates.getSubCategoryName() != null)
            subCategoryRepo.findBySubCategoryNameIgnoreCase(updates.getSubCategoryName());
        if (updates.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(updates.getBrandName());

        return mapToDto(productRepo.save(product));
    });
}

@CachePut(value = "products", key = "#id")
@CacheEvict(value = "products", key = "'allProducts'")
public Optional<ProductDto> patchProduct(Long id, ProductDto updates) {
    return productRepo.findById(id).map(existing -> {
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getPdfUrl() != null) existing.setPdfUrl(updates.getPdfUrl());
        if (updates.getImages() != null) existing.setImages(updates.getImages());
        if (updates.getDescriptionENG() != null)
            existing.setDescriptionENG(String.join("", updates.getDescriptionENG()));
        if (updates.getDescriptionPL() != null)
            existing.setDescriptionPL(String.join("", updates.getDescriptionPL()));
        if (updates.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(updates.getCategoryName());
        if (updates.getSubCategoryName() != null)
            subCategoryRepo.findBySubCategoryNameIgnoreCase(updates.getSubCategoryName());
        if (updates.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(updates.getBrandName());

        Product saved = productRepo.save(existing);
        return mapToDto(saved);
    });
}

@CachePut(value = "products", key = "#id")
@CacheEvict(value = "products", key = "'allProducts'")
public Optional<ProductDto> updateProduct(Long id, ProductDto dto) {

    return productRepo.findById(id).map(existing -> {
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getPdfUrl() != null) existing.setPdfUrl(dto.getPdfUrl());
        if (dto.getImages() != null) existing.setImages(dto.getImages());
        if (dto.getDescriptionENG() != null) existing.setDescriptionENG(String.join("", dto.getDescriptionENG()));
        if (dto.getDescriptionPL() != null) existing.setDescriptionPL(String.join("", dto.getDescriptionPL()));
        if (dto.getCategoryName() != null) categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName());
        if (dto.getSubCategoryName() != null)
            subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName());
        if (dto.getBrandName() != null) brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName());


        Product saved = productRepo.save(existing);
        return mapToDto(saved);
    });
}
}

 */

