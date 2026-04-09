package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        // Zwracamy JSON: {"status": "UP"} zamiast zwykłego tekstu
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}