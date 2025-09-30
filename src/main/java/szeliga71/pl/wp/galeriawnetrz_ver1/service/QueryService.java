package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.QueryRepository;

import java.util.List;

@Service
public class QueryService {

    private final QueryRepository queryRepository;

    public QueryService(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Object[]> runSql(String sql) {
        return queryRepository.executeQuery(sql);
    }
}
