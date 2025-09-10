package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;

import java.util.List;

public class BrandDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long brandId;
    private String brandName;
    private String brandImageUrl;
    private List<String> brandDescriptionPL;
    private List<String> brandDescriptionENG;
    private String brandUrl;
    private String slugName;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImageUrl() {
        return brandImageUrl;
    }

    public void setBrandImageUrl(String brandImageUrl) {
        this.brandImageUrl = brandImageUrl;
    }

    public List<String> getBrandDescriptionPL() {
        return brandDescriptionPL;
    }

    public void setBrandDescriptionPL(List<String> brandDescriptionPL) {
        this.brandDescriptionPL = brandDescriptionPL;
    }

    public List<String> getBrandDescriptionENG() {
        return brandDescriptionENG;
    }

    public void setBrandDescriptionENG(List<String> brandDescriptionENG) {
        this.brandDescriptionENG = brandDescriptionENG;
    }

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slugName) {
        this.slugName = slugName;
    }
}
