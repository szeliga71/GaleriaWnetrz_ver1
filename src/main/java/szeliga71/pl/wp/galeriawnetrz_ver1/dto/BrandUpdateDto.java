package szeliga71.pl.wp.galeriawnetrz_ver1.dto;


import java.util.List;

public class BrandUpdateDto {

    private String brandName;
    private String brandImageUrl;
    private List<String> brandDescriptionPL;
    private List<String> brandDescriptionENG;
    private String brandUrl;
    private String slugName;


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slugName) {
        this.slugName = slugName;
    }

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

    public List<String> getBrandDescriptionENG() {
        return brandDescriptionENG;
    }

    public void setBrandDescriptionENG(List<String> brandDescriptionENG) {
        this.brandDescriptionENG = brandDescriptionENG;
    }

    public List<String> getBrandDescriptionPL() {
        return brandDescriptionPL;
    }

    public void setBrandDescriptionPL(List<String> brandDescriptionPL) {
        this.brandDescriptionPL = brandDescriptionPL;
    }

    public String getBrandImageUrl() {
        return brandImageUrl;
    }

    public void setBrandImageUrl(String brandImageUrl) {
        this.brandImageUrl = brandImageUrl;
    }
}
