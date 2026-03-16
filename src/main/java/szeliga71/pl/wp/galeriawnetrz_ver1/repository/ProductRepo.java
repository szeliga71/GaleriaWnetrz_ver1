package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findByNameIgnoreCaseContaining(String query);

    // Szukanie po nazwie kategorii
    List<Product> findByCategoryCategoryNameIgnoreCase(String categoryName);
    Page<Product> findByCategoryCategoryNameIgnoreCase(String categoryName, Pageable pageable);
    // Szukanie po nazwie podkategorii
    List<Product> findBySubCategorySubCategoryNameIgnoreCase(String subCategoryName);

    // Szukanie po nazwie brandu
    List<Product> findByBrandBrandNameIgnoreCase(String brandName);
    Page<Product> findByBrand_BrandNameIgnoreCase(String brandName,Pageable pageable);
    // Szukanie po kategorii i podkategorii razem
    List<Product> findByCategoryCategoryNameIgnoreCaseAndSubCategorySubCategoryNameIgnoreCase(String categoryName, String subCategoryName);


    // zliczanie
    long countBySubCategory_SubCategoryNameIgnoreCase(String name);

    long countByCategory_CategoryNameIgnoreCase(String name);

    long countByBrand_BrandNameIgnoreCase(String name);

    // Liczba produktów dla brandu
    //long countByBrandNameIgnoreCase(String brandName);

    // Liczba produktów dla kategorii
    //long countByCategoryNameIgnoreCase(String categoryName);

    // Liczba produktów dla podkategorii
    //long countBySubCategoryNameIgnoreCase(String subCategoryName);


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE products RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateProducts();

    Optional<Product> findByNameIgnoreCase(String name);

    //List<Product> findByNameIgnoreCase(String name);

}
/*
package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    // Paginowane wyszukiwanie
    Page<Product> findByCategoryNameIgnoreCase(String categoryName, Pageable pageable);

    Page<Product> findBySubCategoryNameIgnoreCase(String subCategoryName, Pageable pageable);

    Page<Product> findByBrandNameIgnoreCase(String brandName, Pageable pageable);

    Page<Product> findByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(String categoryName, String subCategoryName, Pageable pageable);

    List<Product> findByBrandNameIgnoreCase(String brandName);
    List<Product> findBySubCategoryNameIgnoreCase(String subCategoryName);
    List<Product> findByCategoryNameIgnoreCase(String categoryName);
    List<Product> findByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(String categoryName, String subCategoryName);

    // Bez paginacji
    List<Product> findByNameIgnoreCaseContaining(String query);




*/
