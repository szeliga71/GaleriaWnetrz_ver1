package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.EndpointRequestCounter;
import java.util.Optional;

public interface EndpointRequestCounterRepo extends JpaRepository<EndpointRequestCounter, Long> {
    Optional<EndpointRequestCounter> findByEndpointAndType(String endpoint, String type);
}

