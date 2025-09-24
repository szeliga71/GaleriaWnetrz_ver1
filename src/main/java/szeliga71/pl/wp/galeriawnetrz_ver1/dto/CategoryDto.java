package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CategoryDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long categoryId;
    private String categoryName;
    private String categoryImageUrl;
    private String slugCategoryName;
    private List<SubCategoryDto> subCategories;


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

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getSlugCategoryName() {
        return slugCategoryName;
    }

    public void setSlugCategoryName(String slugCategoryName) {
        this.slugCategoryName = slugCategoryName;
    }

    public List<SubCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryDto> subCategories) {
        this.subCategories = subCategories;
    }
}
