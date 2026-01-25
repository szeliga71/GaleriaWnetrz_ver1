package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.EndpointRequestCounterRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class RequestStatsController {

    private final EndpointRequestCounterRepo repo;

    public RequestStatsController(EndpointRequestCounterRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/requests")
    public List<Map<String, Object>> getAllRequestStats() {
        return repo.findAll().stream()
                .map(rc -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("endpoint", rc.getEndpoint());
                    map.put("type", rc.getType());
                    map.put("successCount", rc.getSuccessCount());
                    map.put("failCount", rc.getFailCount());
                    return map;
                })
                .collect(Collectors.toList());
    }

}

