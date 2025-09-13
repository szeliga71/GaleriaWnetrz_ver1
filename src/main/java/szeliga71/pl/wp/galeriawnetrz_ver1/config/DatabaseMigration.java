package szeliga71.pl.wp.galeriawnetrz_ver1.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigration {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void updateProductColumns() {
        try {
            jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN descriptionpl TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN descriptioneng TYPE TEXT");
            System.out.println("Columns descriptionpl and descriptioneng altered to TEXT successfully.");
        } catch (Exception e) {
            System.err.println("Could not alter columns: " + e.getMessage());
        }
    }
}

