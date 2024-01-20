package com.example.demo.matching.repository;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public abstract class BaseCustomRepository {
    protected final JPAQueryFactory queryFactory;
    protected final EntityManager entityManager;
    protected static final String HAVERSINE_FORMULA = "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))"; // 두 위경도 좌표 사이의 거리 구하는 공식

    protected <T> Querydsl getQuerydsl(Class<T> clazz) {
        PathBuilder<T> builder = new PathBuilderFactory().create(clazz);
        return new Querydsl(entityManager, builder);
    }

    protected <T> PageImpl<T> getPageImpl(Pageable pageable, JPQLQuery<T> query, Class<T> clazz) {
        long totalCount = query.fetchCount();
        List<T> results = getQuerydsl(clazz).applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }
}
