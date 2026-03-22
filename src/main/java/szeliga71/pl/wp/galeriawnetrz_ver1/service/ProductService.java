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
import org.springframework.data.domain.Sort;
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


    @Cacheable(value = "products",
            key = "'allProducts_page_' + #page + '_size_' + #size + '_sort_' + #sort")
    public List<ProductDto> getAllProducts(Integer page, Integer size, String sort) {

        Sort sortObj = createSort(sort); // wydzielamy tylko sortowanie

        // 🔥 brak page/size → zwracamy wszystkie rekordy
        if (page == null || size == null) {
            return productRepo.findAll(sortObj)
                    .stream()
                    .map(this::mapToDto)
                    .toList();
        }

        Pageable pageable = createPageable(page, size, sortObj);
        Page<Product> products = productRepo.findAll(pageable);

        return products.stream().map(this::mapToDto).toList();
    }

    private Sort createSort(String sort) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sort != null && !sort.isBlank()) {
            switch (sort.toLowerCase()) {
                case "az" -> direction = Sort.Direction.ASC;
                case "za" -> direction = Sort.Direction.DESC;
                default -> throw new IllegalArgumentException(
                        "Invalid sort value. Use 'az' or 'za'"
                );
            }
        }

        return Sort.by(direction, "name");
    }
    private Pageable createPageable(Integer page, Integer size, String sort) {

        // domyślnie: sortowanie A → Z
        Sort.Direction direction = Sort.Direction.ASC;

        if (sort != null && !sort.isBlank()) {
            switch (sort.toLowerCase()) {
                case "az" -> direction = Sort.Direction.ASC;
                case "za" -> direction = Sort.Direction.DESC;
                default -> throw new IllegalArgumentException(
                        "Invalid sort value. Use 'az' or 'za'"
                );
            }
        }

        return PageRequest.of(page, size, Sort.by(direction, "name"));
    }
    //===========przeciazona ===========================
    private Pageable createPageable(Integer page, Integer size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
    //===========przeciazona ===========================
    @Cacheable(value = "products", key = "'brand_' + #brandName + '_page_' + #page + '_size_' + #size")
    public List<ProductDto> getProductsByBrandName(String brandName, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<Product> products = productRepo.findByBrand_BrandNameIgnoreCase(brandName, pageable);
        return products.stream().map(this::mapToDto).toList();
    }

    public Optional<ProductDto> getProductById(Long productId) {
        return productRepo.findById(productId).map(this::mapToDto);
    }

    @Cacheable(value = "products", key = "'category_' + #categoryName + '_page_' + #page + '_size_' + #size")
    public List<ProductDto> getProductsByCategoryName(String categoryName, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<Product> products = productRepo.findByCategoryCategoryNameIgnoreCase(categoryName, pageable);
        return products.stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "products", key = "#subCategoryName")
    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productRepo.findBySubCategorySubCategoryNameIgnoreCase(subCategoryName)
                .stream().map(this::mapToDto).toList();
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



@Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto saveProduct(ProductDto dto) {

        Product product = dto.getProductId() != null
                ? productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()))
                : new Product();

        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());

        product.setDescriptionENG(convertText(dto.getDescriptionENG()));
        product.setDescriptionPL(convertText(dto.getDescriptionPL()));

        if (dto.getCategoryName() != null) {
            product.setCategory(
                    categoryRepo.findByCategoryNameIgnoreCase(dto.getCategoryName())
                            .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryName()))
            );
        }

        if (dto.getSubCategoryName() != null) {
            product.setSubCategory(
                    subCategoryRepo.findBySubCategoryNameIgnoreCase(dto.getSubCategoryName())
                            .orElseThrow(() -> new RuntimeException("SubCategory not found: " + dto.getSubCategoryName()))
            );
        }

        if (dto.getBrandName() != null) {
            product.setBrand(
                    brandsRepo.findByBrandNameIgnoreCase(dto.getBrandName())
                            .orElseThrow(() -> new RuntimeException("Brand not found: " + dto.getBrandName()))
            );
        }

        Product saved = productRepo.save(product);

        return mapToDto(saved);
    }




    private String convertText(String text) {
        if (text == null) {
            return null;
        }

        return text
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }


    private ProductDto mapToDto(Product product) {

        ProductDto dto = new ProductDto();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setImages(product.getImages());

        // opis ENG
        if (product.getDescriptionENG() != null) {
            dto.setDescriptionENG(
                    product.getDescriptionENG()
                            .replace("\\n", "\n")
                            .replace("\\r", "\r")
            );
        }

        // opis PL
        if (product.getDescriptionPL() != null) {
            dto.setDescriptionPL(
                    product.getDescriptionPL()
                            .replace("\\n", "\n")
                            .replace("\\r", "\r")
            );
        }

        // BRAND
        if (product.getBrand() != null) {
            dto.setBrandName(product.getBrand().getBrandName());
        }

        // CATEGORY
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        // SUBCATEGORY
        if (product.getSubCategory() != null) {
            dto.setSubCategoryName(product.getSubCategory().getSubCategoryName());
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
        return productRepo.findByCategoryCategoryNameIgnoreCaseAndSubCategorySubCategoryNameIgnoreCase(categoryName, subCategoryName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }




    //  zliczanie
    public long countProductsByBrandName(String brandName) {
        return productRepo.countByBrand_BrandNameIgnoreCase(brandName);
    }

    public long countProductsByCategoryName(String categoryName) {
        return productRepo.countByCategory_CategoryNameIgnoreCase(categoryName);
    }

    public long countProductsBySubCategoryName(String subCategoryName) {
        return productRepo.countBySubCategory_SubCategoryNameIgnoreCase(subCategoryName);
    }


}


