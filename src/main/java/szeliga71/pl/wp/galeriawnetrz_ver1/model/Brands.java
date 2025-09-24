package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;


@Entity
public class Brands {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;
    @Column(unique = true)
    private String brandName;
    private String brandImageUrl;
    @Column(columnDefinition = "TEXT")
    private String brandDescriptionPL;
    @Column(columnDefinition = "TEXT")
    private String brandDescriptionENG;
    private String brandUrl;
    @Column
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
