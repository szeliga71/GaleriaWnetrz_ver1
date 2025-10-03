package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;

import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.BrandsRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.CategoryRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.ProductRepo;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.SubCategoryRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import jakarta.transaction.Transactional;


@Service
public class ProductImportService {

    private final ProductRepo productRepo;
    private final BrandsRepo brandsRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;

    public ProductImportService(ProductRepo productRepo, BrandsRepo brandsRepo,
                                CategoryRepo categoryRepo, SubCategoryRepo subCategoryRepo) {
        this.productRepo = productRepo;
        this.brandsRepo = brandsRepo;
        this.categoryRepo = categoryRepo;
        this.subCategoryRepo = subCategoryRepo;
    }



    @Transactional
    public void importCsv(InputStream inputStream) throws IOException {
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT
                        .withDelimiter(';')   // średnik jako separator
                        .withFirstRecordAsHeader() // pomijamy nagłówki
                        .withIgnoreEmptyLines(true)
                        .withTrim())
        ) {
            for (CSVRecord record : parser) {

                String name = record.get(0).trim();
                String descEng = record.get(1).trim();
                String descPl = record.get(2).trim();
                String pdf = record.get(3).trim();
                String brandName = record.get(4).trim();
                String imagesCsv = record.get(5).trim();
                String subCategoryName = record.get(6).trim();
                String categoryName = record.get(7).trim();

                if (productRepo.findByNameIgnoreCase(name).isPresent()) {
                    // pomijamy duplikaty
                    continue;
                }

                // Brand
                Brands brand = brandsRepo.findByBrandNameIgnoreCase(brandName)
                        .orElseGet(() -> {
                            Brands b = new Brands();
                            b.setBrandName(brandName);
                            b.setSlugName(generateSlug(brandName));
                            return brandsRepo.save(b);
                        });

                // Category
                Category category = categoryRepo.findByCategoryNameIgnoreCase(categoryName)
                        .orElseGet(() -> {
                            Category c = new Category();
                            c.setCategoryName(categoryName);
                            c.setSlugCategoryName(generateSlug(categoryName));
                            return categoryRepo.save(c);
                        });

                // SubCategory
                SubCategory subCategory = subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName)
                        .orElseGet(() -> {
                            SubCategory sc = new SubCategory();
                            sc.setSubCategoryName(subCategoryName);
                            sc.setSlugSubCategoryName(generateSlug(subCategoryName));
                            sc.setCategory(category);
                            return subCategoryRepo.save(sc);
                        });

                // Product
                Product product = new Product();
                product.setName(name);
                product.setDescriptionENG(descEng);
                product.setDescriptionPL(descPl);
                product.setPdfUrl(pdf);
                product.setBrandName(brand.getBrandName());
                product.setCategoryName(category.getCategoryName());
                product.setSubCategoryName(subCategory.getSubCategoryName());

                if (!imagesCsv.isEmpty()) {
                    product.setImages(Arrays.asList(imagesCsv.split(",")));
                }

                productRepo.save(product);
            }
        }
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // zamień spacje/znaki na "-"
                .replaceAll("^-|-$", "");       // usuń "-" z początku/końca
    }
}

