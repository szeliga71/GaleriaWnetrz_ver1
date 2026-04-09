package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.Data;

import java.util.List;

// ProductCreateDto.java - Request (zamiast ProductUpdateDto dla spójności)
@Data
public class ProductCreateDto {
    private String name;
    private String pdfUrl;
    private String brandName;
    private String categoryName;
    private String subCategoryName;
    private String descriptionENG; // Zmienione z List na String, bo w bazie jest String
    private String descriptionPL;
    private List<String> images;
}