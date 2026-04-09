package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.*;

import java.io.*;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final ProductRepo productRepo;
    private final BrandRepo brandRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;

    @Transactional
    public void importCsv(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().withIgnoreEmptyLines(true).withTrim())) {

            for (CSVRecord record : parser) {
                String name = record.get(0).trim();
                String descEng = record.get(1).trim();
                String descPl = record.get(2).trim();
                String pdf = record.get(3).trim();
                String brandName = normalize(record.get(4));
                String imagesCsv = record.get(5).trim();
                String subCategoryName = normalize(record.get(6));
                String categoryName = normalize(record.get(7));

                if (productRepo.findByNameIgnoreCase(name).isPresent()) continue;

              /*  Brand brand = brandRepo.findByNameIgnoreCase(brandName)
                        .orElseGet(() -> brandRepo.saveAndFlush(new Brand(null, brandName, null, null, generateSlug(brandName), null)));*/
                Brand brand = brandRepo.findByNameIgnoreCase(brandName)
                        .orElseGet(() -> {
                            Brand b = new Brand();
                            b.setName(brandName);
                            b.setSlug(generateSlug(brandName));
                            return brandRepo.saveAndFlush(b);
                        });

                /*Category category = categoryRepo.findByNameIgnoreCase(categoryName)
                        .orElseGet(() -> categoryRepo.saveAndFlush(new Category(null, categoryName, generateSlug(categoryName), null, null)));*/
                Category category = categoryRepo.findByNameIgnoreCase(categoryName)
                        .orElseGet(() -> {
                            Category c = new Category();
                            c.setName(categoryName);
                            c.setSlug(generateSlug(categoryName));
                            return categoryRepo.saveAndFlush(c);
                        });

                SubCategory subCategory = subCategoryRepo.findByNameIgnoreCase(subCategoryName)
                        .orElseGet(() -> {
                            SubCategory sc = new SubCategory();
                            sc.setName(subCategoryName);
                            sc.setSlug(generateSlug(subCategoryName));
                            sc.setCategory(category);
                            return subCategoryRepo.saveAndFlush(sc);
                        });

                Product product = new Product();
                product.setName(name);
                product.setDescriptionENG(descEng);
                product.setDescriptionPL(descPl);
                product.setPdfUrl(pdf);
                product.setBrand(brand);
                product.setCategory(category);
                product.setSubCategory(subCategory);

                if (!imagesCsv.isEmpty()) {
                    product.setImages(Arrays.asList(imagesCsv.split(",")));
                }

                productRepo.saveAndFlush(product);
            }
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }

    private String generateSlug(String name) {
        return name == null ? "" : name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}