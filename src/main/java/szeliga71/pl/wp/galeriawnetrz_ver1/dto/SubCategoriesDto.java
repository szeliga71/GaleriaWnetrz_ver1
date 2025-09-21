package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SubCategoriesDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long subCategoryId;
    @NotBlank(message = "SubCategory name cannot be blank")
    private String subCategoryName;
    private String subCategoryImageUrl;
    private String slugSubCategoryName;
    @NotNull(message = "CategoryId is required")
    @Positive(message = "CategoryId must be greater than 0")
    private Long categoryId;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
