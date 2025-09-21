package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import java.util.List;

public class ProductUpdateDto {
    private String name;
    private String pdfUrl;
    private Long brandId;
    private Long categoryId;
    private Long subCategoryId;
    private List<String> descriptionENG;
    private List<String> descriptionPL;
    private List<String> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
