package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import java.util.List;

public class ProductUpdateDto {
    private String name;
    private String pdfUrl;
    private String brandName;
    private String categoryName;
    private String subCategoryName;
    private List<String> descriptionENG;
    private List<String> descriptionPL;
    private List<String> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getDescriptionPL() {
        return descriptionPL;
    }

    public void setDescriptionPL(List<String> descriptionPL) {
        this.descriptionPL = descriptionPL;
    }

    public List<String> getDescriptionENG() {
        return descriptionENG;
    }

    public void setDescriptionENG(List<String> descriptionENG) {
        this.descriptionENG = descriptionENG;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}