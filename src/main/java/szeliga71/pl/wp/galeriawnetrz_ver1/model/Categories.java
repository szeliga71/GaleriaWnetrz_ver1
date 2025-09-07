package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;

@Entity
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;
    private Long subCategoriesId;
    private String categoryImageUrl;


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getSubCategoriesId() {
        return subCategoriesId;
    }

    public void setSubCategoriesId(Long subCategoriesId) {
        this.subCategoriesId = subCategoriesId;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }
}
