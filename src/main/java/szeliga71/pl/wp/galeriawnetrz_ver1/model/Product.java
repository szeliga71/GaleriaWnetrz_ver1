package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(columnDefinition = "TEXT",name = "name",unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String brandName;

    @Column(columnDefinition = "TEXT", name = "descriptionpl")
    private String descriptionPL;

    @Column(columnDefinition = "TEXT", name = "descriptioneng")
    private String descriptionENG;

    @Column(columnDefinition = "TEXT")
    private String categoryName;

    @Column(columnDefinition = "TEXT")
    private String subCategoryName;

    @Column(columnDefinition = "TEXT")
    private String pdfUrl;

    @ElementCollection
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )

    @Column(columnDefinition = "TEXT",name = "image_url")
    private List<String> images;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescriptionENG() {
        return descriptionENG;
    }

    public void setDescriptionENG(String descriptionENG) {
        this.descriptionENG = descriptionENG;
    }

    public String getDescriptionPL() {
        return descriptionPL;
    }

    public void setDescriptionPL(String descriptionPL) {
        this.descriptionPL = descriptionPL;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
