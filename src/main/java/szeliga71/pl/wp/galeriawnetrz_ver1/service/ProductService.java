package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductsRepo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
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
    public void deleteProduct(Long id) {
        productsRepo.deleteById(id);
    }
    public void deleteAllProducts() {
        productsRepo.deleteAll();
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
  /*  @Transactional
    public int importProductsFromCsv(MultipartFile file) {
        List<Products> batch = new ArrayList<>();
        int importedCount = 0;
        int lineNumber = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (firstLine) { // pomiń nagłówek
                    firstLine = false;
                    continue;
                }

                try {
                    ProductDto dto = parseLine(line);
                    Products entity = mapToEntity(dto);
                    batch.add(entity);

                    // batch insert co 500 rekordów
                    if (batch.size() == 500) {
                        productsRepo.saveAll(batch);
                        importedCount += batch.size();
                        batch.clear();
                    }
                } catch (Exception e) {
                    System.err.println("Błąd w linii " + lineNumber + ": " + e.getMessage());
                }
            }

            // zapis ostatniego batcha
            if (!batch.isEmpty()) {
                productsRepo.saveAll(batch);
                importedCount += batch.size();
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage(), e);
        }

        return importedCount;
    }*/

    /*private ProductDto parseLine(String line) {
        String[] fields = line.split(";", -1);

        ProductDto dto = new ProductDto();
        dto.setName(fields[0]);

        dto.setDescriptionPL(fields.length > 1 && !fields[1].isEmpty() ? List.of(fields[1]) : null);
        dto.setDescriptionENG(fields.length > 2 && !fields[2].isEmpty() ? List.of(fields[2]) : null);

        // categoryId
        if (fields.length > 3 && !fields[3].isEmpty()) {
            try {
                dto.setCategoryId(Long.parseLong(fields[3]));
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość categoryId: " + fields[3]);
                dto.setCategoryId(null);
            }
        }

        // subCategoryId
        if (fields.length > 4 && !fields[4].isEmpty()) {
            try {
                dto.setSubCategoryId(Long.parseLong(fields[4]));
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość subCategoryId: " + fields[4]);
                dto.setSubCategoryId(null);
            }
        }

        dto.setPdfUrl(fields.length > 5 ? fields[5] : null);

        // brandId
        if (fields.length > 6 && !fields[6].isEmpty()) {
            try {
                dto.setBrandId(Long.parseLong(fields[6]));
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość brandId: " + fields[6]);
                dto.setBrandId(null);
            }
        }

        if (fields.length > 7 && !fields[7].isEmpty()) {
            dto.setImages(List.of(fields[7].split(",")));
        }

        return dto;
    }*/

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

        // Name
        Integer idx = headerMap.get("name");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setName(fields[idx]);
        }

        // Description PL
        idx = headerMap.get("descriptionpl");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setDescriptionPL(List.of(fields[idx]));
        }

        // Description ENG
        idx = headerMap.get("descriptioneng");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setDescriptionENG(List.of(fields[idx]));
        }

        // CategoryId
        idx = headerMap.get("categoryid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                dto.setCategoryId(Long.parseLong(fields[idx]));
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość categoryId: " + fields[idx]);
            }
        }

        // SubCategoryId
        idx = headerMap.get("subcategoryid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                dto.setSubCategoryId(Long.parseLong(fields[idx]));
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość subCategoryId: " + fields[idx]);
            }
        }

        // PDF URL
        idx = headerMap.get("pdfurl");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setPdfUrl(fields[idx]);
        }

        // BrandId
        idx = headerMap.get("brandid");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            try {
                dto.setBrandId(Long.parseLong(fields[idx]));
            } catch (NumberFormatException e) {
                System.err.println("Nieprawidłowa wartość brandId: " + fields[idx]);
            }
        }

        // Images
        idx = headerMap.get("images");
        if (idx != null && idx < fields.length && !fields[idx].isEmpty()) {
            dto.setImages(List.of(fields[idx].split(",")));
        }

        return dto;
    }



}





