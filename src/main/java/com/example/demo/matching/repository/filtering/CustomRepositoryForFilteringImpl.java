package com.example.demo.matching.repository.filtering;

import com.example.demo.entity.Matching;
import com.example.demo.matching.dto.FilterRequestDto;
import com.example.demo.matching.filter.Region;
import com.example.demo.matching.repository.BaseCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.entity.QMatching.matching;

@Repository
public class CustomRepositoryForFilteringImpl extends BaseCustomRepository implements CustomRepositoryForFiltering {

    public CustomRepositoryForFilteringImpl(JPAQueryFactory queryFactory, EntityManager entityManager) {
        super(queryFactory, entityManager);
    }

    @Override
    public PageImpl<Matching> findAllByFilter(FilterRequestDto filterRequestDto, Pageable pageable) {
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

    //TODO: 리팩토링 전
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
