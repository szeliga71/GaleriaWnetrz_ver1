package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

public class BrandCreateDto {
    private String brandName;
    private String brandImageUrl;
    private String brandDescriptionPL; // zwykły string
    private String brandDescriptionENG; // zwykły string
    private String brandUrl;
    private String slugName;



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

    public String getBrandDescriptionPL() {
        return brandDescriptionPL;
    }

    public void setBrandDescriptionPL(String brandDescriptionPL) {
        this.brandDescriptionPL = brandDescriptionPL;
    }

    public String getBrandDescriptionENG() {
        return brandDescriptionENG;
    }

    public void setBrandDescriptionENG(String brandDescriptionENG) {
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

