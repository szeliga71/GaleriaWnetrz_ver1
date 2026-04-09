package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrandDto {
    private Long brandId;
    private String brandName;
    private String brandImageUrl;
    private List<String> brandDescriptionPL; // Lista stringów dla frontendu
    private List<String> brandDescriptionENG;
    private String brandUrl;
    private String slugName;
}
