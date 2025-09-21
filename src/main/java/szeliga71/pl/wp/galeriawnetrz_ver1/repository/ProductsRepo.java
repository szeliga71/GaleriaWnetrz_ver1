package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;

import java.util.List;


@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {

    List<Products> findByCategory_CategoryId(Long categoryId);

    List<Products> findBySubCategory_SubCategoryId(Long subCategoryId);

    List<Products> findByBrand_BrandId(Long brandId);

    List<Products> findByCategory_CategoryNameIgnoreCase(String categoryName);

    List<Products> findBySubCategory_SubCategoryNameIgnoreCase(String subCategoryName);

    List<Products> findByCategory_CategoryIdAndSubCategory_SubCategoryId(Long categoryId, Long subCategoryId);

    List<Products> findByNameIgnoreCase(String name);

    List<Products> findByCategory_CategoryNameAndSubCategory_SubCategoryName(String categoryName, String subCategoryName);

    List<Products> findByBrand_BrandNameIgnoreCase(String brandName);

}
