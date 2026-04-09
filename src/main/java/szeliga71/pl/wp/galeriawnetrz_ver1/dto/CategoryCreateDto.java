package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateDto {
    @NotBlank(message = "Nazwa jest wymagana")
    private String categoryName;
    private String categoryImageUrl;
}
