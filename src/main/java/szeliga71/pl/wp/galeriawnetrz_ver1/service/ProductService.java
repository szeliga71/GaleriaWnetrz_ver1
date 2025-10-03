package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(this::mapToDto).toList();
    }

    public Optional<ProductDto> getProductById(Long productId) {
        return productRepo.findById(productId).map(this::mapToDto);
    }

    @Cacheable(value = "products", key = "#categoryName")
    public List<ProductDto> getProductsByCategoryName(String categoryName) {
        return productRepo.findByCategoryNameIgnoreCase(categoryName).stream().map(this::mapToDto).toList();
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

   /* private Product mapToEntity(ProductDto dto) {
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
    }*/


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

/*
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
    }*/


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
    public List<ProductDto> getProductsByBrandName(String brandName) {
        return productRepo.findByBrandNameIgnoreCase(brandName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

}

/*   kod poprawiny przez chat gpt  mam watpliwosci do ew sprawdzenia
package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    // ---------------------- READ operations (cacheable) ----------------------

    // cache key = 'allProducts'
    @Cacheable(value = "products", key = "'allProducts'")
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(this::mapToDto).toList();
    }

    // cache key = 'product:id:{id}'
    @Cacheable(value = "products", key = "'product:id:' + #id")
    public Optional<ProductDto> getProductById(Long id) {
        return productRepo.findById(id).map(this::mapToDto);
    }

    // cache key = 'category:{categoryNameLower}'
    @Cacheable(value = "products", key = "'category:' + (#categoryName == null ? 'null' : #categoryName.toLowerCase())")
    public List<ProductDto> getProductsByCategoryName(String categoryName) {
        return productRepo.findByCategoryNameIgnoreCase(categoryName).stream().map(this::mapToDto).toList();
    }

    // cache key = 'subcategory:{subCategoryNameLower}'
    @Cacheable(value = "products", key = "'subcategory:' + (#subCategoryName == null ? 'null' : #subCategoryName.toLowerCase())")
    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productRepo.findBySubCategoryNameIgnoreCase(subCategoryName).stream().map(this::mapToDto).toList();
    }

    // cache key = 'brand:{brandNameLower}'
    @Cacheable(value = "products", key = "'brand:' + (#brandName == null ? 'null' : #brandName.toLowerCase())")
    public List<ProductDto> getProductsByBrandName(String brandName) {
        return productRepo.findByBrandNameIgnoreCase(brandName).stream().map(this::mapToDto).toList();
    }

    // ---------------------- WRITE operations (evict caches to keep consistency) ----------------------
    // Simple approach: evict all entries in "products" cache on writes (consistent).

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto saveProduct(ProductDto dto) {
        Product entity = dto.getProductId() != null
                ? productRepo.findById(dto.getProductId()).orElse(new Product())
                : new Product();

        // fill fields from DTO (use dto values)
        entity.setName(dto.getName());
        entity.setPdfUrl(dto.getPdfUrl());
        entity.setImages(dto.getImages() == null ? null : new ArrayList<>(dto.getImages()));

        entity.setDescriptionENG(joinAndEscape(dto.getDescriptionENG()));
        entity.setDescriptionPL(joinAndEscape(dto.getDescriptionPL()));

        // find-or-create brand/category/subcategory (only store names in product as String fields)
        if (dto.getBrandName() != null && !dto.getBrandName().isBlank()) {
            Brands b = findOrCreateBrand(dto.getBrandName().trim());
            entity.setBrandName(b.getBrandName());
        }

        if (dto.getCategoryName() != null && !dto.getCategoryName().isBlank()) {
            Category c = findOrCreateCategory(dto.getCategoryName().trim());
            entity.setCategoryName(c.getCategoryName());
        }

        if (dto.getSubCategoryName() != null && !dto.getSubCategoryName().isBlank()) {
            // if subcategory should be linked to category: try to find category first
            Category cat = null;
            if (entity.getCategoryName() != null) {
                cat = categoryRepo.findByCategoryNameIgnoreCase(entity.getCategoryName()).orElse(null);
            }
            SubCategory sc = findOrCreateSubCategory(dto.getSubCategoryName().trim(), cat);
            entity.setSubCategoryName(sc.getSubCategoryName());
        }

        Product saved = productRepo.save(entity);
        return mapToDto(saved);
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

    // Update by name — evict caches to keep everything consistent
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto updateProductByName(String productName, ProductDto dto) {
        Product product = productRepo.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // apply updates from DTO (only non-null)
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPdfUrl() != null) product.setPdfUrl(dto.getPdfUrl());
        if (dto.getImages() != null) product.setImages(new ArrayList<>(dto.getImages()));
        if (dto.getDescriptionENG() != null) product.setDescriptionENG(joinAndEscape(dto.getDescriptionENG()));
        if (dto.getDescriptionPL() != null) product.setDescriptionPL(joinAndEscape(dto.getDescriptionPL()));

        if (dto.getBrandName() != null && !dto.getBrandName().isBlank()) {
            Brands b = findOrCreateBrand(dto.getBrandName().trim());
            product.setBrandName(b.getBrandName());
        }

        if (dto.getCategoryName() != null && !dto.getCategoryName().isBlank()) {
            Category c = findOrCreateCategory(dto.getCategoryName().trim());
            product.setCategoryName(c.getCategoryName());
        }

        if (dto.getSubCategoryName() != null && !dto.getSubCategoryName().isBlank()) {
            Category cat = null;
            if (product.getCategoryName() != null) {
                cat = categoryRepo.findByCategoryNameIgnoreCase(product.getCategoryName()).orElse(null);
            }
            SubCategory sc = findOrCreateSubCategory(dto.getSubCategoryName().trim(), cat);
            product.setSubCategoryName(sc.getSubCategoryName());
        }

        Product saved = productRepo.save(product);
        return mapToDto(saved);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Optional<ProductDto> patchProductByName(String productName, ProductDto updates) {
        return productRepo.findByNameIgnoreCase(productName).map(product -> {
            if (updates.getName() != null) product.setName(updates.getName());
            if (updates.getPdfUrl() != null) product.setPdfUrl(updates.getPdfUrl());
            if (updates.getImages() != null) product.setImages(new ArrayList<>(updates.getImages()));
            if (updates.getDescriptionENG() != null) product.setDescriptionENG(joinAndEscape(updates.getDescriptionENG()));
            if (updates.getDescriptionPL() != null) product.setDescriptionPL(joinAndEscape(updates.getDescriptionPL()));

            if (updates.getBrandName() != null && !updates.getBrandName().isBlank()) {
                Brands b = findOrCreateBrand(updates.getBrandName().trim());
                product.setBrandName(b.getBrandName());
            }

            if (updates.getCategoryName() != null && !updates.getCategoryName().isBlank()) {
                Category c = findOrCreateCategory(updates.getCategoryName().trim());
                product.setCategoryName(c.getCategoryName());
            }

            if (updates.getSubCategoryName() != null && !updates.getSubCategoryName().isBlank()) {
                Category cat = null;
                if (product.getCategoryName() != null) {
                    cat = categoryRepo.findByCategoryNameIgnoreCase(product.getCategoryName()).orElse(null);
                }
                SubCategory sc = findOrCreateSubCategory(updates.getSubCategoryName().trim(), cat);
                product.setSubCategoryName(sc.getSubCategoryName());
            }

            Product saved = productRepo.save(product);
            return mapToDto(saved);
        });
    }

    @CacheEvict(value = "products", allEntries = true)
    public Optional<ProductDto> patchProduct(Long id, ProductDto updates) {
        return productRepo.findById(id).map(existing -> {
            if (updates.getName() != null) existing.setName(updates.getName());
            if (updates.getPdfUrl() != null) existing.setPdfUrl(updates.getPdfUrl());
            if (updates.getImages() != null) existing.setImages(new ArrayList<>(updates.getImages()));
            if (updates.getDescriptionENG() != null) existing.setDescriptionENG(joinAndEscape(updates.getDescriptionENG()));
            if (updates.getDescriptionPL() != null) existing.setDescriptionPL(joinAndEscape(updates.getDescriptionPL()));

            if (updates.getCategoryName() != null && !updates.getCategoryName().isBlank()) {
                Category c = findOrCreateCategory(updates.getCategoryName().trim());
                existing.setCategoryName(c.getCategoryName());
            }

            if (updates.getSubCategoryName() != null && !updates.getSubCategoryName().isBlank()) {
                Category cat = null;
                if (existing.getCategoryName() != null) {
                    cat = categoryRepo.findByCategoryNameIgnoreCase(existing.getCategoryName()).orElse(null);
                }
                SubCategory sc = findOrCreateSubCategory(updates.getSubCategoryName().trim(), cat);
                existing.setSubCategoryName(sc.getSubCategoryName());
            }

            if (updates.getBrandName() != null && !updates.getBrandName().isBlank()) {
                Brands b = findOrCreateBrand(updates.getBrandName().trim());
                existing.setBrandName(b.getBrandName());
            }

            Product saved = productRepo.save(existing);
            return mapToDto(saved);
        });
    }

    // ---------------------- Helpers ----------------------

    private String joinAndEscape(List<String> parts) {
        if (parts == null) return null;
        // join without adding extra separators; escape newline chars
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p == null) continue;
            String s = p.replace("\n", "\\n").replace("\r", "\\r");
            sb.append(s);
        }
        return sb.toString();
    }

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setImages(product.getImages());

        dto.setDescriptionENG(splitText(product.getDescriptionENG(), 100));
        dto.setDescriptionPL(splitText(product.getDescriptionPL(), 100));

        dto.setCategoryName(product.getCategoryName());
        dto.setSubCategoryName(product.getSubCategoryName());
        dto.setBrandName(product.getBrandName());
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

    // Find-or-create helpers for related tables
    private Brands findOrCreateBrand(String brandName) {
        if (brandName == null || brandName.isBlank()) return null;
        Optional<Brands> existing = brandsRepo.findByBrandNameIgnoreCase(brandName);
        if (existing.isPresent()) return existing.get();

        Brands b = new Brands();
        b.setBrandName(brandName);
        b.setSlugName(generateSlug(brandName));
        // other brand fields can be left null
        return brandsRepo.save(b);
    }

    private Category findOrCreateCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) return null;
        Optional<Category> existing = categoryRepo.findByCategoryNameIgnoreCase(categoryName);
        if (existing.isPresent()) return existing.get();

        Category c = new Category();
        c.setCategoryName(categoryName);
        c.setSlugCategoryName(generateSlug(categoryName));
        return categoryRepo.save(c);
    }

    private SubCategory findOrCreateSubCategory(String subCategoryName, Category category) {
        if (subCategoryName == null || subCategoryName.isBlank()) return null;
        Optional<SubCategory> existing = subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName);
        if (existing.isPresent()) {
            // ensure it belongs to given category if category provided
            SubCategory sc = existing.get();
            if (category == null || sc.getCategory() == null || Objects.equals(sc.getCategory().getCategoryId(), category.getCategoryId())) {
                return sc;
            }
            // otherwise create a new one (name unique constraint may break — but your SubCategory table has unique on name; if so, you need different business rule)
        }

        SubCategory sc = new SubCategory();
        sc.setSubCategoryName(subCategoryName);
        sc.setSlugSubCategoryName(generateSlug(subCategoryName));
        if (category != null) sc.setCategory(category);
        return subCategoryRepo.save(sc);
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}

 */


