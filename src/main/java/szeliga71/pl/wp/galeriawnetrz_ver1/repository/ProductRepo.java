package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

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
    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    // Szukanie po nazwie podkategorii
    List<Product> findBySubCategoryNameIgnoreCase(String subCategoryName);

    // Szukanie po nazwie brandu
    List<Product> findByBrandNameIgnoreCase(String brandName);

    // Szukanie po kategorii i podkategorii razem
    List<Product> findByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(String categoryName, String subCategoryName);


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE products RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateProducts();

    Optional<Product> findByNameIgnoreCase(String name);

    //List<Product> findByNameIgnoreCase(String name);

}
