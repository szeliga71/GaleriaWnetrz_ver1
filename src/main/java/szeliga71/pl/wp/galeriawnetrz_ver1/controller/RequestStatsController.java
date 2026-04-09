package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.EndpointRequestCounterRepo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor // Automatyczny konstruktor dla repo
public class RequestStatsController {

    private final EndpointRequestCounterRepo repo;

    @GetMapping("/requests")
    public ResponseEntity<List<Map<String, Object>>> getAllRequestStats() {
        List<Map<String, Object>> stats = repo.findAll().stream()
                .map(rc -> Map.of(
                        "endpoint", (Object) rc.getEndpoint(),
                        "type", rc.getType(),
                        "successCount", rc.getSuccessCount(),
                        "failCount", rc.getFailCount()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(stats);
    }
}