package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.QueryResultsDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.QueryService;

import java.util.List;

@RestController
@RequestMapping("/api/query")
@RequiredArgsConstructor // Automatyczny konstruktor dla queryService
public class QueryController {

    private final QueryService queryService;

    @GetMapping("/{query}")
    public ResponseEntity<QueryResultsDto> query(@PathVariable String query) {
        // Logika walidacji długości zapytania
        if (query == null || query.trim().length() < 3) {
            // Zwracamy pusty DTO zamiast błędu, aby frontend mógł to łatwo obsłużyć
            return ResponseEntity.ok(new QueryResultsDto(List.of(), List.of(), List.of(), List.of()));
        }

        return ResponseEntity.ok(queryService.search(query));
    }
}