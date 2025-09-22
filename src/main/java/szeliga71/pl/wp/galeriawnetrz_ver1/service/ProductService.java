package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategories;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoriesRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoriesRepo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private CategoriesRepo categoriesRepo;
    @Autowired
    private BrandsRepo brandsRepo;
    @Autowired
    private SubCategoriesRepo subCategoriesRepo;

    @PersistenceContext
    private EntityManager em;

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
        return productsRepo.findByCategory_CategoryId(categoryId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByBrandId(Long brandId) {
        return productsRepo.findByBrand_BrandId(brandId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsBySubCategory(Long subCategoryId) {
        return productsRepo.findBySubCategory_SubCategoryId(subCategoryId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByCategoryName(String categoryName) {
        return productsRepo.findByCategory_CategoryNameIgnoreCase(categoryName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsBySubCategoryName(String subCategoryName) {
        return productsRepo.findBySubCategory_SubCategoryNameIgnoreCase(subCategoryName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    public void deleteProduct(Long id) {
        productsRepo.deleteById(id);
    }

    public void deleteAllProducts() {
        productsRepo.deleteAll();
        em.createNativeQuery("ALTER SEQUENCE products_product_id_seq RESTART WITH 1").executeUpdate();
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
        Products product;

        if (dto.getProductId() != null) {
            product = productsRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));
        } else {
            product = new Products();
        }

        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());
        product.setDescriptionENG(dto.getDescriptionENG() != null ? String.join("", dto.getDescriptionENG()) : null);
        product.setDescriptionPL(dto.getDescriptionPL() != null ? String.join("", dto.getDescriptionPL()) : null);

        if (dto.getCategoryId() != null) {
            categoriesRepo.findById(dto.getCategoryId()).ifPresent(product::setCategory);
        }

        if (dto.getSubCategoryId() != null) {
            subCategoriesRepo.findById(dto.getSubCategoryId()).ifPresent(product::setSubCategory);
        }
        if (dto.getBrandId() != null) {
            brandsRepo.findById(dto.getBrandId()).ifPresent(product::setBrand);
        }


        Products saved = productsRepo.save(product);
        return mapToDto(saved);
    }


    private ProductDto mapToDto(Products product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPdfUrl(product.getPdfUrl());
        dto.setImages(product.getImages());
        dto.setDescriptionENG(splitText(product.getDescriptionENG(), 100));
        dto.setDescriptionPL(splitText(product.getDescriptionPL(), 100));

        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null);
        dto.setSubCategoryId(product.getSubCategory() != null ? product.getSubCategory().getSubCategoryId() : null);

        dto.setBrandId(product.getBrand() != null ? product.getBrand().getBrandId() : null);

        return dto;
    }

    private Products mapToEntity(ProductDto dto) {
        Products product = new Products();
        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());
        product.setDescriptionENG(dto.getDescriptionENG() != null ? String.join("", dto.getDescriptionENG()) : null);
        product.setDescriptionPL(dto.getDescriptionPL() != null ? String.join("", dto.getDescriptionPL()) : null);

        if (dto.getCategoryId() != null) {
            categoriesRepo.findById(dto.getCategoryId()).ifPresent(product::setCategory);
        }
        if (dto.getSubCategoryId() != null) {
            subCategoriesRepo.findById(dto.getSubCategoryId()).ifPresent(product::setSubCategory);
        }
        if (dto.getBrandId() != null) {
            brandsRepo.findById(dto.getBrandId()).ifPresent(product::setBrand);
        }

        return product;
    }
    @Transactional
    public int importProductsFromCsv(MultipartFile file) {
        List<Products> batch = new ArrayList<>();
        int importedCount = 0;
        int lineNumber = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            Map<String, Integer> headerMap = new HashMap<>();
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (firstLine) {
                    String[] headers = line.toLowerCase().split(";", -1);
                    for (int i = 0; i < headers.length; i++) {
                        headerMap.put(headers[i].trim(), i);
                    }
                    firstLine = false;
                    continue;
                }

                try {
                    ProductDto dto = parseLineWithHeaders(line, headerMap);

                    // 1️⃣ Sprawdź kategorię
                    Optional<Categories> categoryOpt = categoriesRepo.findById(dto.getCategoryId());
                    if (categoryOpt.isEmpty()) {
                        Categories category = new Categories();
                        category.setCategoryId(dto.getCategoryId());
                        category.setCategoryName(dto.getName());
                        categoriesRepo.save(category);
                    }

                    // 2️⃣ Sprawdź subkategorię
                    Optional<SubCategories> subCategoryOpt = subCategoriesRepo.findById(dto.getSubCategoryId());
                    if (subCategoryOpt.isEmpty()) {
                        SubCategories subCategory = new SubCategories();
                        subCategory.setSubCategoryId(dto.getSubCategoryId());
                        subCategory.setSubCategoryName(dto.getName());
                        //subCategory.setCategories(dto.getCategoryId());
                        subCategoriesRepo.save(subCategory);
                    }

                    // 3️⃣ Mapowanie produktu
                    Products entity = mapToEntity(dto);
                    batch.add(entity);

                    if (batch.size() == 500) {
                        productsRepo.saveAll(batch);
                        importedCount += batch.size();
                        batch.clear();
                    }

                } catch (Exception e) {
                    System.err.println("Błąd w linii " + lineNumber + ": " + e.getMessage());
                }
            }

            if (!batch.isEmpty()) {
                productsRepo.saveAll(batch);
                importedCount += batch.size();
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage(), e);
        }

        return importedCount;
    }


   /* @Transactional
    public int importProductsFromCsv(MultipartFile file) {
        List<Products> batch = new ArrayList<>();
        int importedCount = 0;
        int lineNumber = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            Map<String, Integer> headerMap = new HashMap<>();
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (firstLine) {

                    String[] headers = line.toLowerCase().split(";", -1);
                    for (int i = 0; i < headers.length; i++) {
                        headerMap.put(headers[i].trim(), i);
                    }
                    firstLine = false;
                    continue;
                }

                try {
                    ProductDto dto = parseLineWithHeaders(line, headerMap);
                    Products entity = mapToEntity(dto);
                    batch.add(entity);

                    if (batch.size() == 500) {
                        productsRepo.saveAll(batch);
                        importedCount += batch.size();
                        batch.clear();
                    }
                } catch (Exception e) {
                    System.err.println("Błąd w linii " + lineNumber + ": " + e.getMessage());
                }
            }

            if (!batch.isEmpty()) {
                productsRepo.saveAll(batch);
                importedCount += batch.size();
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage(), e);
        }

        return importedCount;
    }*/


    /*private ProductDto parseLineWithHeaders(String line, java.util.Map<String, Integer> headerMap) {
        String[] fields = line.split(";", -1);
        ProductDto dto = new ProductDto();
        Integer idx;

        idx = headerMap.get("name");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            // zamiast ustawiać product.name, ustawiamy brandName
            Long brandId = findOrCreateBrandByBrandName(fields[idx]);
            dto.setBrandId(brandId);

            // product.name zostaje puste, chyba że inny plik CSV dostarczy np. "productname"
            dto.setName(null);
        }

        //idx = headerMap.get("productname");
        //if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
          //  dto.setName(fields[idx]);
        //}
        idx = headerMap.get("brandname");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            Long branId = findOrCreateBrandByBrandName(fields[idx]);
            dto.setBrandId(branId);
        }



        String[] descPLKeys = {"descriptionpl", "description_pl", "descriptionPl"};
        for (String key : descPLKeys) {
            idx = headerMap.get(key);
            if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
                dto.setDescriptionPL(List.of(fields[idx]));
                break;
            }
        }

        String[] descENGKeys = {"desc", "descriptioneng", "description_eng", "descriptionEng"};
        for (String key : descENGKeys) {
            idx = headerMap.get(key);
            if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
                dto.setDescriptionENG(List.of(fields[idx]));
                break;
            }
        }

        idx = headerMap.get("categoryid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                dto.setCategoryId(Long.parseLong(fields[idx]));
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Nieprawidłowa wartość categoryId: " + fields[idx] + " → ustawiono null");
                dto.setCategoryId(null);
            }
        }

        idx = headerMap.get("categoryname");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            Long catId = findOrCreateCategoryByName(fields[idx]);
            dto.setCategoryId(catId);
        }

        idx = headerMap.get("subcategoryid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                dto.setSubCategoryId(Long.parseLong(fields[idx]));
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Nieprawidłowa wartość subCategoryId: " + fields[idx] + " → ustawiono null");
                dto.setSubCategoryId(null);
            }
        }

        idx = headerMap.get("subcategoryname");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            Long subId = findOrCreateSubCategoryByName(fields[idx], dto.getCategoryId());
            dto.setSubCategoryId(subId);
        }

        // PDF URL
        idx = headerMap.get("pdf");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setPdfUrl(fields[idx]);
        }



        idx = headerMap.get("images");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setImages(List.of(fields[idx].split(",")));
        } else {
            dto.setImages(null);
        }

        return dto;
    }*/
    private ProductDto parseLineWithHeaders(String line, java.util.Map<String, Integer> headerMap) {
        String[] fields = line.split(";", -1);
        ProductDto dto = new ProductDto();
        Integer idx;


        // ✅ Product name
        idx = headerMap.get("name");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setName(fields[idx].trim());
        }
        // ✅ BrandName → findOrCreate
        idx = headerMap.get("brandname");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            Long brandId = findOrCreateBrandByBrandName(fields[idx]);
            dto.setBrandId(brandId);
        }



        // ✅ Category by Name
        idx = headerMap.get("categoryname");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            Long catId = findOrCreateCategoryByName(fields[idx]);
            dto.setCategoryId(catId);
        }

        // ✅ SubCategory by Name (powiązana z CategoryId)
        idx = headerMap.get("subcategoryname");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            Long subId = findOrCreateSubCategoryByName(fields[idx], dto.getCategoryId());
            dto.setSubCategoryId(subId);
        }

        // ✅ Description PL
        String[] descPLKeys = {"descriptionpl", "description_pl", "descriptionPl"};
        for (String key : descPLKeys) {
            idx = headerMap.get(key);
            if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
                dto.setDescriptionPL(List.of(fields[idx]));
                break;
            }
        }

        // ✅ Description ENG
        String[] descENGKeys = {"desc", "descriptioneng", "description_eng", "descriptionEng"};
        for (String key : descENGKeys) {
            idx = headerMap.get(key);
            if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
                dto.setDescriptionENG(List.of(fields[idx]));
                break;
            }
        }

        // ✅ PDF URL
        idx = headerMap.get("pdf");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setPdfUrl(fields[idx]);
        }

        // ✅ Images (rozdzielone przecinkami)
        idx = headerMap.get("images");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setImages(List.of(fields[idx].split(",")));
        } else {
            dto.setImages(null);
        }

        return dto;
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

        return categoriesRepo.findAll().stream()
                .filter(c -> c.getCategoryName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .map(c -> c.getCategoryId())
                .orElseGet(() -> {
                    // Tworzymy nową kategorię
                    var newCat = new szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories();
                    newCat.setCategoryName(name.trim());
                    categoriesRepo.save(newCat);
                    return newCat.getCategoryId();
                });
    }

    private Long findOrCreateSubCategoryByName(String name, Long categoryId) {
        if (name == null || name.isBlank() || categoryId == null) return null;

        return subCategoriesRepo.findAll().stream()
                .filter(sc -> sc.getSubCategoryName().equalsIgnoreCase(name.trim()) &&
                        sc.getCategory() != null &&
                        Objects.equals(sc.getCategory().getCategoryId(), categoryId))
                .findFirst()
                .map(sc -> sc.getSubCategoryId())
                .orElseGet(() -> {
                    // Tworzymy nową subkategorię
                    var cat = categoriesRepo.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found for subcategory"));
                    var newSub = new szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategories();
                    newSub.setSubCategoryName(name.trim());
                    newSub.setCategory(cat);
                    subCategoriesRepo.save(newSub);
                    return newSub.getSubCategoryId();
                });
    }

    public List<ProductDto> getProductsByCategoryAndSubCategory(Long categoryId, Long subCategoryId) {
        return productsRepo.findByCategory_CategoryIdAndSubCategory_SubCategoryId(categoryId, subCategoryId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductByName(String productName) {
        return productsRepo.findByNameIgnoreCase(productName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByCategoryNameAndSubCategoryName(String categoryName, String subCategoryName) {
        return productsRepo.findByCategory_CategoryNameAndSubCategory_SubCategoryName(categoryName, subCategoryName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ProductDto> getProductsByBrandName(String brandName) {
        return productsRepo.findByBrand_BrandNameIgnoreCase(brandName)
                .stream()
                .map(this::mapToDto)
                .toList();
    }
}





