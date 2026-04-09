package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.ProductDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.QueryResultsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final BrandRepo brandRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final ProductRepo productRepo;
    private final QueryRepository queryRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "queryCache", key = "#query")
    public QueryResultsDto search(String query) {

        List<ProductDto> brandResults = brandRepo.findByNameIgnoreCaseContaining(query).stream()
                .flatMap(b -> productRepo.findByBrand_NameIgnoreCase(b.getName(), Pageable.unpaged()).stream())
                .map(this::mapToDto).collect(Collectors.toList());

        List<ProductDto> categoryResults = categoryRepo.findByNameIgnoreCaseContaining(query).stream()
                .flatMap(c -> productRepo.findByCategory_NameIgnoreCase(c.getName(), Pageable.unpaged()).stream())
                .map(this::mapToDto).collect(Collectors.toList());

        List<ProductDto> subCategoryResults = subCategoryRepo.findByNameIgnoreCaseContaining(query).stream()
                .flatMap(sc -> productRepo.findBySubCategory_NameIgnoreCase(sc.getName()).stream())
                .map(this::mapToDto).collect(Collectors.toList());

        List<ProductDto> productResults = productRepo.findByNameIgnoreCaseContaining(query).stream()
                .map(this::mapToDto).collect(Collectors.toList());

        return new QueryResultsDto(brandResults, categoryResults, subCategoryResults, productResults);
    }

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setBrandName(product.getBrand().getName());
        dto.setCategoryName(product.getCategory().getName());
        dto.setSubCategoryName(product.getSubCategory().getName());
        dto.setImages(product.getImages());
        return dto;
    }

    public List<Object[]> runSql(String sql) {
        return queryRepository.executeQuery(sql);
    }
}