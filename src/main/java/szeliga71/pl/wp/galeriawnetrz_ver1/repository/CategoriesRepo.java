package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Categories;

@Repository
public interface CategoriesRepo extends JpaRepository<Categories, Long> {
    boolean existsBySlugCategoryName(String slugCategoryName);
}
