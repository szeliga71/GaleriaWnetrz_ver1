package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // pomijamy nagłówek
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 8) continue;

                String name = fields[0].trim();
                String descEng = fields[1].trim();
                String descPl = fields[2].trim();
                String pdf = fields[3].trim();
                String brandName = fields[4].trim();
                String imagesCsv = fields[5].trim();
                String subCategoryName = fields[6].trim();
                String categoryName = fields[7].trim();


                if (productRepo.findByNameIgnoreCase(name).isPresent()) {
                    // Jeśli produkt istnieje, pomijamy go
                    continue;
                }

                // Sprawdzenie/utworzenie brandu
                Brands brand = brandsRepo.findByBrandNameIgnoreCase(brandName)
                        .orElseGet(() -> {
                            Brands b = new Brands();
                            b.setBrandName(brandName);
                            b.setSlugName(generateSlug(brandName));
                            return brandsRepo.save(b);
                        });

                // Sprawdzenie/utworzenie kategorii
                Category category = categoryRepo.findByCategoryNameIgnoreCase(categoryName)
                        .orElseGet(() -> {
                            Category c = new Category();
                            c.setCategoryName(categoryName);
                            c.setSlugCategoryName(generateSlug(categoryName));
                            return categoryRepo.save(c);
                        });

                // Sprawdzenie/utworzenie subkategorii
                SubCategory subCategory = subCategoryRepo.findBySubCategoryNameIgnoreCase(subCategoryName)
                        .orElseGet(() -> {
                            SubCategory sc = new SubCategory();
                            sc.setSubCategoryName(subCategoryName);
                            sc.setSlugSubCategoryName(generateSlug(subCategoryName));
                            sc.setCategory(category);
                            return subCategoryRepo.save(sc);
                        });

                // Tworzenie produktu
                Product product = new Product();
                product.setName(name);
                product.setDescriptionENG(descEng);
                product.setDescriptionPL(descPl);
                product.setPdfUrl(pdf);
                product.setBrandName(brand.getBrandName());
                product.setCategoryName(category.getCategoryName());
                product.setSubCategoryName(subCategory.getSubCategoryName());
                product.setImages(Arrays.asList(imagesCsv.split(","))); // obrazy oddzielone przecinkiem

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

