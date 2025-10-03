package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.QueryResultsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.*;

import java.util.List;

@Transactional
@Service
public class QueryService {

    private final BrandsRepo brandRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final ProductRepo productRepo;
    private final QueryRepository queryRepository;

    public QueryService(BrandsRepo brandRepo,
                        CategoryRepo categoryRepo,
                        SubCategoryRepo subCategoryRepo,
                        ProductRepo productRepo,
                        QueryRepository queryRepository) {
        this.brandRepo = brandRepo;
        this.categoryRepo = categoryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.productRepo = productRepo;
        this.queryRepository = queryRepository;
    }

    @Transactional
    @Cacheable(value = "queryCache", key = "#query")
    public QueryResultsDto search(String query) {
        // Brand -> Product
        List<Product> brandResults = brandRepo.findByBrandNameIgnoreCaseContaining(query).stream()
                .flatMap(b -> productRepo.findByBrandNameIgnoreCase(b.getBrandName()).stream())
                .toList();

        // Category -> Product
        List<Product> categoryResults = categoryRepo.findByCategoryNameIgnoreCaseContaining(query).stream()
                .flatMap(c -> productRepo.findByCategoryNameIgnoreCase(c.getCategoryName()).stream())
                .toList();

        // SubCategory -> Product
        List<Product> subCategoryResults = subCategoryRepo.findBySubCategoryNameIgnoreCaseContaining(query).stream()
                .flatMap(sc -> productRepo.findBySubCategoryNameIgnoreCase(sc.getSubCategoryName()).stream())
                .toList();

        // Product name directly
        List<Product> productResults = productRepo.findByNameIgnoreCaseContaining(query);

        return new QueryResultsDto(brandResults, categoryResults, subCategoryResults, productResults);
    }

    //poprzednia metoda same zapytanie sql
    public List<Object[]> runSql(String sql) {
        return queryRepository.executeQuery(sql);
    }
}
