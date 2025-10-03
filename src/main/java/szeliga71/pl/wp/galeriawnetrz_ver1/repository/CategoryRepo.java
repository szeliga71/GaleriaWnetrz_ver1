package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import szeliga71.pl.wp.galeriawnetrz_ver1.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    boolean existsBySlugCategoryName(String slugCategoryName);

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

    List<Category> findByCategoryNameIgnoreCaseContaining(String query);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE categories RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateCategories();
}
