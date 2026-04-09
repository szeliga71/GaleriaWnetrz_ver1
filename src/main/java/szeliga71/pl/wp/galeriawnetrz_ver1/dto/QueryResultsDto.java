package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResultsDto {
    private List<ProductDto> brandResults;
    private List<ProductDto> categoryResults;
    private List<ProductDto> subCategoriesResults;
    private List<ProductDto> productResults;
}

