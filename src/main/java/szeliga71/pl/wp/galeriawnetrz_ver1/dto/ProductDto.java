package szeliga71.pl.wp.galeriawnetrz_ver1.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProductDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long productId;
    private String name;
    private List<String> descriptionENG;
    private List<String> descriptionPL;
    @NotNull
    @Min(1)
    private Long categoryId;
    @NotNull
    @Min(1)
    private Long subCategoryId;
    private String pdfUrl;
    @NotNull
    @Min(1)
    private Long brandId;
    //private String brandName;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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
}


