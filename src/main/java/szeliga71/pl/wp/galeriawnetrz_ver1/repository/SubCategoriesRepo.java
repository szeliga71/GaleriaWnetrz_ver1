package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.SubCategories;

import java.util.List;


public interface SubCategoriesRepo extends JpaRepository<SubCategories, Long> {
        List<SubCategories> findByCategory_CategoryId(Long categoryId);
        boolean existsBySlugSubCategoryName(String slugSubCategoryName);
    }

