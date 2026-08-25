package com.addiction;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 테스트 간 데이터 격리를 위해 현재 H2 스키마의 모든 테이블 데이터를 비운다.
     * FK 삭제 순서에 영향을 받지 않도록 참조 무결성을 일시적으로 해제한다.
     */
    public void clean() {
        // daily_smoking_push_schedule처럼 users를 참조하는 테이블이 있어도 TRUNCATE할 수 있도록 한다.
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        try {
            findTableNames().forEach(this::truncate);
        } finally {
            // 정리 도중 예외가 발생해도 이후 테스트에서 FK 제약조건이 유지되도록 반드시 복구한다.
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

    // H2 시스템 테이블을 제외하고 현재 애플리케이션 스키마의 실제 테이블만 조회한다.
    private List<String> findTableNames() {
        return jdbcTemplate.queryForList("""
                SELECT TABLE_NAME
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = CURRENT_SCHEMA()
                  AND TABLE_TYPE = 'BASE TABLE'
                """, String.class);
    }

    // 테이블명은 메타데이터에서 조회하지만, 식별자 인용으로 안전하게 SQL을 구성한다.
    private void truncate(String tableName) {
        jdbcTemplate.execute("TRUNCATE TABLE " + quoteIdentifier(tableName));
    }

    private String quoteIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }
}
