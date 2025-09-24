package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = true)//fetch = FetchType.LAZY
    @JoinColumn(name = "brand_id", nullable = true)//,insertable = false, updatable = false)
    private Brands brand;

    @Column(columnDefinition = "TEXT", name = "descriptionpl")
    private String descriptionPL;

    @Column(columnDefinition = "TEXT", name = "descriptioneng")
    private String descriptionENG;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne(optional = true)
    @JoinColumn(name = "sub_category_id", nullable = true)
    private SubCategory subCategory;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @ElementCollection
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image_url")
    private List<String> images;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionPL() {
        return descriptionPL;
    }

    public void setDescriptionPL(String descriptionPL) {
        this.descriptionPL = descriptionPL;
    }

    public String getDescriptionENG() {
        return descriptionENG;
    }

    public void setDescriptionENG(String descriptionENG) {
        this.descriptionENG = descriptionENG;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Brands getBrand() {
        return brand;
    }

    public void setBrand(Brands brand) {
        this.brand = brand;
    }

}
