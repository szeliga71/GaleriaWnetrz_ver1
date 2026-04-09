
/*package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final BrandsRepo brandsRepo;
    private final SubCategoryRepo subCategoryRepo;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo, BrandsRepo brandsRepo, SubCategoryRepo subCategoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.brandsRepo = brandsRepo;
        this.subCategoryRepo = subCategoryRepo;
    }

    // -------------------- UTILS --------------------
    private Pageable createPageable(Integer page, Integer size, String sort) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sort != null && !sort.isBlank()) {
            switch (sort.toLowerCase()) {
                case "za" -> direction = Sort.Direction.DESC;
                case "az" -> direction = Sort.Direction.ASC;
                default -> throw new IllegalArgumentException("Invalid sort value. Use 'az' or 'za'");
            }
        }
        return (page != null && size != null) ? PageRequest.of(page, size, Sort.by(direction, "name")) : Pageable.unpaged();
    }

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setImages(product.getImages());
        dto.setDescriptionENG(product.getDescriptionENG());
        dto.setDescriptionPL(product.getDescriptionPL());
        dto.setBrandName(product.getBrand() != null ? product.getBrand().getBrandName() : null);
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null);
        dto.setSubCategoryName(product.getSubCategory() != null ? product.getSubCategory().getSubCategoryName() : null);
        return dto;
    }

    private void updateRelations(Product product, ProductDto dto) {
        // Brand
        if (dto.getBrandName() != null) {
            Brands brand = brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName())
                    .orElseGet(() -> {
                        Brands b = new Brands();
                        b.setBrandName(dto.getBrandName());
                        b.setSlugName(generateSlug(dto.getBrandName()));
                        return brandsRepo.save(b);
                    });
            product.setBrand(brand);
        }

        // Category
        if (dto.getCategoryName() != null) {
            Category category = categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName())
                    .orElseGet(() -> {
                        Category c = new Category();
                        c.setCategoryName(dto.getCategoryName());
                        c.setSlugCategoryName(generateSlug(dto.getCategoryName()));
                        return categoryRepo.save(c);
                    });
            product.setCategory(category);
        }

        // SubCategory
        if (dto.getSubCategoryName() != null) {
            SubCategory subCategory = subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName())
                    .orElseGet(() -> {
                        SubCategory sc = new SubCategory();
                        sc.setSubCategoryName(dto.getSubCategoryName());
                        sc.setSlugSubCategoryName(generateSlug(dto.getSubCategoryName()));
                        sc.setCategory(product.getCategory()); // przypisanie do kategorii
                        return subCategoryRepo.save(sc);
                    });
            product.setSubCategory(subCategory);
        }
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    // -------------------- CRUD --------------------
    @Cacheable(value = "products", key = "'allProducts_page_' + #page + '_size_' + #size + '_sort_' + #sort")
    public List<ProductDto> getAllProducts(Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findAll(pageable).stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "products", key = "'brand_' + #brandName + '_page_' + #page + '_size_' + #size + '_sort_' + #sort")
    public List<ProductDto> getProductsByBrandName(String brandName, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findByBrand_BrandNameIgnoreCase(brandName, pageable).stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "products", key = "'category_' + #categoryName + '_page_' + #page + '_size_' + #size + '_sort_' + #sort")
    public List<ProductDto> getProductsByCategoryName(String categoryName, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findByCategoryCategoryNameIgnoreCase(categoryName, pageable).stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "products", key = "'subcategory_' + #subCategoryName")
    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productRepo.findBySubCategorySubCategoryNameIgnoreCase(subCategoryName).stream().map(this::mapToDto).toList();
    }

    public Optional<ProductDto> getProductById(Long productId) {
        return productRepo.findById(productId).map(this::mapToDto);
    }

    public List<ProductDto> getProductByName(String productName) {
        return productRepo.findByNameIgnoreCase(productName).stream().map(this::mapToDto).toList();
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto saveProduct(ProductDto dto) {
        Product product = dto.getProductId() != null
                ? productRepo.findById(dto.getProductId())
                .orElse(new Product())
                : new Product();

        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());
        product.setDescriptionENG(dto.getDescriptionENG());
        product.setDescriptionPL(dto.getDescriptionPL());

        updateRelations(product, dto);
        return mapToDto(productRepo.save(product));
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPdfUrl() != null) product.setPdfUrl(dto.getPdfUrl());
        if (dto.getImages() != null) product.setImages(dto.getImages());
        if (dto.getDescriptionENG() != null) product.setDescriptionENG(dto.getDescriptionENG());
        if (dto.getDescriptionPL() != null) product.setDescriptionPL(dto.getDescriptionPL());

        updateRelations(product, dto);
        return mapToDto(productRepo.save(product));
    }

    @CacheEvict(value = "products", allEntries = true)
    public Optional<ProductDto> patchProduct(Long id, ProductDto dto) {
        return productRepo.findById(id).map(product -> {
            if (dto.getName() != null) product.setName(dto.getName());
            if (dto.getPdfUrl() != null) product.setPdfUrl(dto.getPdfUrl());
            if (dto.getImages() != null) product.setImages(dto.getImages());
            if (dto.getDescriptionENG() != null) product.setDescriptionENG(dto.getDescriptionENG());
            if (dto.getDescriptionPL() != null) product.setDescriptionPL(dto.getDescriptionPL());

            updateRelations(product, dto);
            return mapToDto(productRepo.save(product));
        });
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteAllAndReset() {
        productRepo.truncateProducts();
    }

    // -------------------- COUNTERS --------------------
    public long countProductsByBrandName(String brandName) {
        return productRepo.countByBrand_BrandNameIgnoreCase(brandName);
    }

    public long countProductsByCategoryName(String categoryName) {
        return productRepo.countByCategory_CategoryNameIgnoreCase(categoryName);
    }

    public long countProductsBySubCategoryName(String subCategoryName) {
        return productRepo.countBySubCategory_SubCategoryNameIgnoreCase(subCategoryName);
    }
}*/
package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductCreateDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final BrandRepo brandRepo;
    private final SubCategoryRepo subCategoryRepo;

    // -------------------- POMOCNICZE (UTILS) --------------------

    private Pageable createPageable(Integer page, Integer size, String sort) {
        Sort.Direction direction = (sort != null && sort.equalsIgnoreCase("za"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Domyślnie sortujemy po nazwie produktu
        return (page != null && size != null)
                ? PageRequest.of(page, size, Sort.by(direction, "name"))
                : Pageable.unpaged();
    }

    private ProductDto mapToDto(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setImages(product.getImages());
        dto.setDescriptionENG(product.getDescriptionENG());
        dto.setDescriptionPL(product.getDescriptionPL());

        // Mapowanie nazw z powiązanych encji
        if (product.getBrand() != null) dto.setBrandName(product.getBrand().getName());
        if (product.getCategory() != null) dto.setCategoryName(product.getCategory().getName());
        if (product.getSubCategory() != null) dto.setSubCategoryName(product.getSubCategory().getName());

        return dto;
    }

    private void updateRelations(Product product, String brandName, String categoryName, String subCategoryName) {
        if (brandName != null) {
            brandRepo.findByNameIgnoreCase(brandName).ifPresent(product::setBrand);
        }
        if (categoryName != null) {
            categoryRepo.findByNameIgnoreCase(categoryName).ifPresent(product::setCategory);
        }
        if (subCategoryName != null) {
            subCategoryRepo.findByNameIgnoreCase(subCategoryName).ifPresent(product::setSubCategory);
        }
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    // -------------------- CRUD --------------------

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductCreateDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());
        product.setDescriptionPL(dto.getDescriptionPL());
        product.setDescriptionENG(dto.getDescriptionENG());

        updateRelations(product, dto.getBrandName(), dto.getCategoryName(), dto.getSubCategoryName());

        return mapToDto(productRepo.save(product));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'all_p' + #page + '_s' + #size + '_sort' + #sort")
    public List<ProductDto> getAllProducts(Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findAll(pageable)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductById(Long id) {
        return productRepo.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductByName(String name) {
        return productRepo.findByNameIgnoreCase(name)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductDto updateProduct(Long id, ProductCreateDto dto) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPdfUrl() != null) product.setPdfUrl(dto.getPdfUrl());
        if (dto.getImages() != null) product.setImages(dto.getImages());
        if (dto.getDescriptionPL() != null) product.setDescriptionPL(dto.getDescriptionPL());
        if (dto.getDescriptionENG() != null) product.setDescriptionENG(dto.getDescriptionENG());

        updateRelations(product, dto.getBrandName(), dto.getCategoryName(), dto.getSubCategoryName());

        return mapToDto(productRepo.save(product));
    }

    @CacheEvict(value = "products", allEntries = true)
    public Optional<ProductDto> patchProduct(Long id, ProductCreateDto dto) {
        return productRepo.findById(id).map(product -> {
            if (dto.getName() != null) product.setName(dto.getName());
            if (dto.getPdfUrl() != null) product.setPdfUrl(dto.getPdfUrl());
            if (dto.getImages() != null) product.setImages(dto.getImages());
            if (dto.getDescriptionPL() != null) product.setDescriptionPL(dto.getDescriptionPL());
            if (dto.getDescriptionENG() != null) product.setDescriptionENG(dto.getDescriptionENG());

            updateRelations(product, dto.getBrandName(), dto.getCategoryName(), dto.getSubCategoryName());
            return mapToDto(productRepo.save(product));
        });
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepo.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteAllAndReset() {
        productRepo.truncateProducts();
    }

    // -------------------- FILTROWANIE I LICZNIKI --------------------

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'cat_' + #categoryName + '_p' + #page")
    public List<ProductDto> getProductsByCategoryName(String categoryName, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findByCategory_NameIgnoreCase(categoryName, pageable)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'brand_' + #brandName + '_p' + #page")
    public List<ProductDto> getProductsByBrandName(String brandName, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findByBrand_NameIgnoreCase(brandName, pageable)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productRepo.findBySubCategory_NameIgnoreCase(subCategoryName)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public long countProductsByBrandName(String brandName) {
        return productRepo.countByBrand_NameIgnoreCase(brandName);
    }

    public long countProductsByCategoryName(String categoryName) {
        return productRepo.countByCategory_NameIgnoreCase(categoryName);
    }

    public long countProductsBySubCategoryName(String subCategoryName) {
        return productRepo.countBySubCategory_NameIgnoreCase(subCategoryName);
    }
}


