package com.example.demo.matching.repository.filtering;

import com.example.demo.entity.Matching;
import com.example.demo.matching.dto.FilterDto;
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
    public PageImpl<Matching> findAllByFilter(FilterDto filterDto, Pageable pageable) {
        JPQLQuery<Matching> matchingList = queryFactory.selectFrom(matching)
                    .where(
                            date(filterDto),
                            region(filterDto),
                            matchingType(filterDto),
                            ageGroup(filterDto),
                            ntrp(filterDto)
                    );

        return getPageImpl(pageable, matchingList, Matching.class);
    }

    private BooleanExpression date(FilterDto filterDto){
        if(filterDto.getDate().isEmpty()) {
            return null;
        }
        LocalDate dateOfFilter = LocalDate.parse(filterDto.getDate());
        return matching.date.eq(dateOfFilter);
    }

    private BooleanExpression ageGroup(FilterDto filterDto){
        if(filterDto.getAgeGroups().isEmpty()) {
            return null;
        }
        return matching.age.in(filterDto.getAgeGroups());
    }

    private BooleanExpression region(FilterDto filterDto){
        if(filterDto.getRegions().isEmpty()) {
            return null;
        }

        List<String> regions = filterDto.getRegions().stream()
                .map(Region::getKorean).toList();
        StringExpression locationExpression = Expressions.asString(matching.location);

        return regions.stream()
                .map(region -> locationExpression.likeIgnoreCase("%" + region + "%"))
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private BooleanExpression matchingType(FilterDto filterDto){
        if(filterDto.getMatchingTypes().isEmpty()) {
            return null;
        }
        return matching.matchingType.in(filterDto.getMatchingTypes());
    }

    private BooleanExpression ntrp(FilterDto filterDto){
        if(filterDto.getNtrps().isEmpty()) {
            return null;
        }
        return matching.ntrp.in(filterDto.getNtrps());
    }
}
