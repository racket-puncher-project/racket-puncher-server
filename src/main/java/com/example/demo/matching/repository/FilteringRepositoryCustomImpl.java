package com.example.demo.matching.repository;

import com.example.demo.entity.Matching;
import com.example.demo.matching.dto.FilterRequestDto;
import com.example.demo.matching.dto.LocationDto;
import com.example.demo.matching.filter.Region;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.entity.QMatching.matching;

public class FilteringRepositoryCustomImpl implements FilteringRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public FilteringRepositoryCustomImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public PageImpl<Matching> searchWithFilter(FilterRequestDto filterRequestDto, Pageable pageable) {
        JPQLQuery<Matching> matchingList;
        if(filterRequestDto.getLocation() == null){
            matchingList =  queryFactory.selectFrom(matching)
                    .where(
                            date(filterRequestDto),
                            region(filterRequestDto),
                            matchingType(filterRequestDto),
                            ageGroup(filterRequestDto),
                            ntrp(filterRequestDto)
                    );
        } else {
            String haversineFormula = "ST_Distance_Sphere(point({0}, {1}), point("
                    + filterRequestDto.getLocation().getLon() + ", " + filterRequestDto.getLocation().getLat() + "))";
            matchingList = queryFactory.selectFrom(matching)
                    .where(
                            date(filterRequestDto),
                            region(filterRequestDto),
                            matchingType(filterRequestDto),
                            ageGroup(filterRequestDto),
                            ntrp(filterRequestDto)
                    )
                    .orderBy(Expressions.stringTemplate(haversineFormula, matching.lon, matching.lat).asc());
        }

        return getPageImpl(pageable, matchingList, Matching.class);
    }

    private Querydsl getQuerydsl(Class clazz) {    // 1)
        PathBuilder<Matching> builder = new PathBuilderFactory().create(clazz);
        return new Querydsl(entityManager, builder);
    }

    private <T> PageImpl<T> getPageImpl(Pageable pageable, JPQLQuery<T> query, Class clazz) {    // 2)
        long totalCount = query.fetchCount();
        List<T> results = getQuerydsl(clazz).applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public PageImpl<Matching> searchWithin(LocationDto center, LocationDto northEastBound, LocationDto southWestBound, Pageable pageable) {
        String haversineFormula = "ST_Distance_Sphere(point({0}, {1}), point("
                + center.getLon() + ", " + center.getLat() + "))";

        JPQLQuery<Matching> matchingList =  queryFactory.selectFrom(matching)
                .where(
                        within(southWestBound.getLat(), northEastBound.getLat(), southWestBound.getLon(), northEastBound.getLon())
                )
                .orderBy(Expressions.stringTemplate(haversineFormula, matching.lon, matching.lat).asc());

        JPQLQuery<Matching> total = queryFactory.selectFrom(matching)
                .where(
                        within(southWestBound.getLat(), northEastBound.getLat(), southWestBound.getLon(), northEastBound.getLon())
                );

        return getPageImpl(pageable, matchingList, Matching.class);
    }

    private BooleanExpression within(Double latLowerBound, Double latUpperBound, Double lonLeftBound, Double lonRightBound){
        return matching.lat.between(latLowerBound, latUpperBound)
                .and(matching.lon.between(lonLeftBound, lonRightBound));
    }

    private BooleanExpression date(FilterRequestDto filterRequestDto){
        if(filterRequestDto.getFilters().getDate().length() == 0) {
            return null;
        }
        LocalDate dateOfFilter = LocalDate.parse(filterRequestDto.getFilters().getDate());
        return matching.date.eq(dateOfFilter);
    }

    private BooleanExpression ageGroup(FilterRequestDto filterRequestDto){
        if(filterRequestDto.getFilters().getAgeGroups().size() == 0) {
            return null;
        }
        return matching.age.in(filterRequestDto.getFilters().getAgeGroups());
    }

    private BooleanExpression region(FilterRequestDto filterRequestDto){
        if(filterRequestDto.getFilters().getRegions().size() == 0) {
            return null;
        }

        // 영어를 한국어로 - 매칭의 위치를 StringExpression로 - 지역이 포함되어있는지 boolean 배열로 - boolean 배열중에 하나라도 true 있으면 true
        List<String> regions = filterRequestDto.getFilters().getRegions().stream()
                .map(Region::getKorean).toList();
        StringExpression locationExpression = Expressions.asString(matching.location);

        BooleanExpression[] conditions = regions.stream()
                .map(region -> locationExpression.likeIgnoreCase("%" + region + "%"))
                .toArray(BooleanExpression[]::new);

        return Expressions.anyOf(conditions);
    }

    private BooleanExpression matchingType(FilterRequestDto filterRequestDto){
        if(filterRequestDto.getFilters().getMatchingTypes().size() == 0) {
            return null;
        }
        return matching.matchingType.in(filterRequestDto.getFilters().getMatchingTypes());
    }

    private BooleanExpression ntrp(FilterRequestDto filterRequestDto){
        if(filterRequestDto.getFilters().getNtrps().size() == 0) {
            return null;
        }
        return matching.ntrp.in(filterRequestDto.getFilters().getNtrps());
    }
}
