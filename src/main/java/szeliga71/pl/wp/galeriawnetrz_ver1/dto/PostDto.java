package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

// PostDto.java - Response (to co wysyłamy do frontendu)
@Data
public class PostDto {
    private UUID postId;
    private String title;
    private String coverImageUrl;
    private List<String> content; // splitowany tekst
    private List<String> images;
}

