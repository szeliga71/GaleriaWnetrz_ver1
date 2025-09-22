package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Brands;

import java.util.Optional;

@Repository
public interface BrandsRepo extends JpaRepository<Brands, Long> {
    Optional<Brands> findByBrandName(String brandName);

}
