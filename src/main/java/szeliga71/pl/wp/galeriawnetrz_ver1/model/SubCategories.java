package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_categories")
public class SubCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;
    private String subCategoryName;
    private String subCategoryImageUrl;
    private String slugSubCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryImageUrl() {
        return subCategoryImageUrl;
    }

    public void setSubCategoryImageUrl(String subCategoryImageUrl) {
        this.subCategoryImageUrl = subCategoryImageUrl;
    }

    public String getSlugSubCategoryName() {
        return slugSubCategoryName;
    }

    public void setSlugSubCategoryName(String slugSubCategoryName) {
        this.slugSubCategoryName = slugSubCategoryName;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }


}


