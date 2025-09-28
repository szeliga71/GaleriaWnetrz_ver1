package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_categories")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;
    @Column(columnDefinition = "TEXT",unique = true)
    private String subCategoryName;
    @Column(columnDefinition = "TEXT")
    private String subCategoryImageUrl;
    @Column(columnDefinition = "TEXT",unique = true)
    private String slugSubCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}


