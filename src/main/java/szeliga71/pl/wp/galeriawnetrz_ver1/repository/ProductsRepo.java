package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Products;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepo extends JpaRepository<Products, Long> {

   /* @Query("SELECT p FROM Products p WHERE p.productId = :id")
    Optional<Products> findByProductId(@Param("id") Long id);*/

    Optional<Products> findByCategoryId(Long categoryId);

    Optional<Products> findByBrandId(Long brandId);
}
