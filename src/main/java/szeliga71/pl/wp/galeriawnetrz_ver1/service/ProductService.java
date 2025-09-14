package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
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
        dto.setDescriptionENG(splitText(product.getDescriptionENG(), 100));
        dto.setDescriptionPL(splitText(product.getDescriptionPL(), 100));
        dto.setCategoryId(product.getCategoryId());
        dto.setSubCategoryId(product.getSubCategoryId());
        return dto;
    }


    private Products mapToEntity(ProductDto dto) {
        Products product = new Products();
        product.setName(dto.getName());
        product.setPdfUrl(dto.getPdfUrl());
        product.setImages(dto.getImages());
        product.setDescriptionENG(dto.getDescriptionENG() != null ? String.join("", dto.getDescriptionENG()) : null);
        product.setDescriptionPL(dto.getDescriptionPL() != null ? String.join("", dto.getDescriptionPL()) : null);

        // categoryId – walidacja
        if (dto.getCategoryId() != null && categoriesRepo.existsById(dto.getCategoryId())) {
            product.setCategoryId(dto.getCategoryId());
        } else {
            product.setCategoryId(null);
            System.err.println("⚠️ Nieprawidłowe categoryId: " + dto.getCategoryId() + " → ustawiono null");
        }

        // subCategoryId – walidacja
        if (dto.getSubCategoryId() != null && subCategoriesRepo.existsById(dto.getSubCategoryId())) {
            product.setSubCategoryId(dto.getSubCategoryId());
        } else {
            product.setSubCategoryId(null);
            System.err.println("⚠️ Nieprawidłowe subCategoryId: " + dto.getSubCategoryId() + " → ustawiono null");
        }

        // brandId – walidacja
        if (dto.getBrandId() != null && brandsRepo.existsById(dto.getBrandId())) {
            product.setBrandId(dto.getBrandId());
        } else {
            product.setBrandId(null);
            System.err.println("⚠️ Nieprawidłowe brandId: " + dto.getBrandId() + " → ustawiono null");
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
                    // mapujemy nagłówki: nazwaKolumny -> indeks
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
    }

    private ProductDto parseLineWithHeaders(String line, java.util.Map<String, Integer> headerMap) {
        String[] fields = line.split(";", -1);
        ProductDto dto = new ProductDto();
        Integer idx;

        // Name
        idx = headerMap.get("name");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setName(fields[idx]);
        }

        // Description PL
        String[] descPLKeys = {"descriptionpl", "description_pl", "descriptionPl"};
        for (String key : descPLKeys) {
            idx = headerMap.get(key);
            if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
                dto.setDescriptionPL(List.of(fields[idx]));
                break;
            }
        }

        // Description ENG
        String[] descENGKeys = {"descriptioneng", "description_eng", "descriptionEng"};
        for (String key : descENGKeys) {
            idx = headerMap.get(key);
            if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
                dto.setDescriptionENG(List.of(fields[idx]));
                break;
            }
        }

        // CategoryId
        idx = headerMap.get("categoryid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                Long catId = Long.parseLong(fields[idx]);
                dto.setCategoryId(catId);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Nieprawidłowa wartość categoryId: " + fields[idx] + " → ustawiono null");
                dto.setCategoryId(null);
            }
        } else {
            dto.setCategoryId(null);
        }

        // SubCategoryId
        idx = headerMap.get("subcategoryid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                Long subId = Long.parseLong(fields[idx]);
                dto.setSubCategoryId(subId);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Nieprawidłowa wartość subCategoryId: " + fields[idx] + " → ustawiono null");
                dto.setSubCategoryId(null);
            }
        } else {
            dto.setSubCategoryId(null);
        }

        // PDF URL
        idx = headerMap.get("pdf");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setPdfUrl(fields[idx]);
        }

        // BrandId
        idx = headerMap.get("brandid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                Long brandId = Long.parseLong(fields[idx]);
                dto.setBrandId(brandId);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Nieprawidłowa wartość brandId: " + fields[idx] + " → ustawiono null");
                dto.setBrandId(null);
            }
        } else {
            dto.setBrandId(null);
        }

        // Images
        idx = headerMap.get("images");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setImages(List.of(fields[idx].split(",")));
        } else {
            dto.setImages(null);
        }

        return dto;
    }



}





