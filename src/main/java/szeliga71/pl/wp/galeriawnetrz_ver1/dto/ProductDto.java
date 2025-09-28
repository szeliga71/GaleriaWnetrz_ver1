package szeliga71.pl.wp.galeriawnetrz_ver1.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.List;

public class ProductDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long productId;
    private String name;
    private String brandName;

    private List<String> descriptionENG;
    private List<String> descriptionPL;
    private String categoryName;
    private String subCategoryName;
    private String pdfUrl;
    private List<String> images;
    private String coverImageUrl;


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

    public List<String> getDescriptionENG() {
        return descriptionENG;
    }

    public void setDescriptionENG(List<String> descriptionENG) {
        this.descriptionENG = descriptionENG;
    }

    public List<String> getDescriptionPL() {
        return descriptionPL;
    }

    public void setDescriptionPL(List<String> descriptionPL) {
        this.descriptionPL = descriptionPL;
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
        if (images != null && !images.isEmpty()) {
            this.coverImageUrl = images.get(0);
        }
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}


