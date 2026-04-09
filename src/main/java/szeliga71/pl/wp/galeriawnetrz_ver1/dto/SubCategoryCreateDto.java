package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryCreateDto {
    private String subCategoryName;
    private String subCategoryImageUrl;
    private Long categoryId;
}