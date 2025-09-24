package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategory;

import java.util.List;
import java.util.Optional;


public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategory_CategoryId(Long categoryId);

    boolean existsBySlugSubCategoryName(String slugSubCategoryName);

    Optional<SubCategory> findBySubCategoryNameIgnoreCase(String subCategoryName);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE sub_categories RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateSubCategory();


}

