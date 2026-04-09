package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "endpoint_request_counter")
public class EndpointRequestCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint;
    private String type;
    private long successCount;
    private long failCount;
}
