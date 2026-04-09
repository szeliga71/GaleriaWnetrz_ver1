package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.Data;

import java.util.List;

// PostCreateDto.java - Request (to co przychodzi z frontendu)
@Data
public class PostCreateDto {
    private String title;
    private String coverImageUrl;
    private String content; // Prostsze przesyłanie jako jeden tekst
    private List<String> images;
}
