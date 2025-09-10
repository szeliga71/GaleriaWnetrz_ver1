package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;

import java.util.List;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {

    List<Products> findByCategoryId(Long categoryId);
    List<Products> findBySubCategoryId(Long subCategoryId);
    List<Products> findByBrandId(Long brandId);

    //List<Products> findBySubCategory_Category_CategoryId(Long categoryId);
    //List<Products> findBySubCategory_subCategoriesId(Long subCategoriesId);


}
