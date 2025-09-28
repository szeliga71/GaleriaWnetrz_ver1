package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;

import java.util.Optional;

@Repository
public interface BrandsRepo extends JpaRepository<Brands, Long> {
    Optional<Brands> findByBrandName(String brandName);

    Optional<Brands> findByBrandNameIgnoreCase(String brandName);


    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE brands RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateBrands();

}
