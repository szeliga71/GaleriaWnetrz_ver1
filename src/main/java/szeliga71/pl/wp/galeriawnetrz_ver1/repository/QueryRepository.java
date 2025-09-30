package szeliga71.pl.wp.galeriawnetrz_ver1.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> executeQuery(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}
