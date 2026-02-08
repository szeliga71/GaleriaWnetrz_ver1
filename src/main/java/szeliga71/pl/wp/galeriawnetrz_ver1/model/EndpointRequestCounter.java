package szeliga71.pl.wp.galeriawnetrz_ver1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "endpoint_request_counter")
public class EndpointRequestCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint; // np. /api/products/all
    private String type;     // Swagger / API
    private long successCount;
    private long failCount;

    // Gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public long getSuccessCount() { return successCount; }
    public void setSuccessCount(long successCount) { this.successCount = successCount; }
    public long getFailCount() { return failCount; }
    public void setFailCount(long failCount) { this.failCount = failCount; }
}
