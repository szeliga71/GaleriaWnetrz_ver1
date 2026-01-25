package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.EndpointRequestCounter;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.EndpointRequestCounterRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EndpointRequestCounterService {

    private final EndpointRequestCounterRepo repo;

    public EndpointRequestCounterService(EndpointRequestCounterRepo repo) {
        this.repo = repo;
    }

    public void increment(String endpoint, String type, boolean success) {
        EndpointRequestCounter counter = repo.findByEndpointAndType(endpoint, type)
                .orElseGet(() -> {
                    EndpointRequestCounter rc = new EndpointRequestCounter();
                    rc.setEndpoint(endpoint);
                    rc.setType(type);
                    rc.setSuccessCount(0);
                    rc.setFailCount(0);
                    return rc;
                });

        if (success) {
            counter.setSuccessCount(counter.getSuccessCount() + 1);
        } else {
            counter.setFailCount(counter.getFailCount() + 1);
        }

        repo.save(counter);
    }
}
