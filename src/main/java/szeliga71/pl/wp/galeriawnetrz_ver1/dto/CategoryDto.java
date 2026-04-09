package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id; // Zmienione z categoryId na id dla spójności
    private String name; // Zmienione z categoryName na name
    private String categoryImageUrl;
    private String slug; // Zmienione ze slugCategoryName na slug
    private List<SubCategoryDto> subCategories;
}