package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

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
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{query}")
    public ResponseEntity<QueryResultsDto> query(@PathVariable String query) {
        if (query.length() < 3) {
            return ResponseEntity.ok(new QueryResultsDto(List.of(), List.of(), List.of(), List.of()));
            //return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(queryService.search(query));
    }
}
