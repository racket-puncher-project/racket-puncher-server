package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatching is a Querydsl query type for Matching
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMatching extends EntityPathBase<Matching> {

    private static final long serialVersionUID = 1768365838L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatching matching = new QMatching("matching");

    public final EnumPath<com.example.demo.type.AgeGroup> age = createEnum("age", com.example.demo.type.AgeGroup.class);

    public final NumberPath<Integer> confirmedNum = createNumber("confirmedNum", Integer.class);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> cost = createNumber("cost", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isReserved = createBoolean("isReserved");

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final StringPath location = createString("location");

    public final StringPath locationImg = createString("locationImg");

    public final NumberPath<Double> lon = createNumber("lon", Double.class);

    public final EnumPath<com.example.demo.type.MatchingType> matchingType = createEnum("matchingType", com.example.demo.type.MatchingType.class);

    public final EnumPath<com.example.demo.type.Ntrp> ntrp = createEnum("ntrp", com.example.demo.type.Ntrp.class);

    public final DateTimePath<java.time.LocalDateTime> recruitDueDateTime = createDateTime("recruitDueDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> recruitNum = createNumber("recruitNum", Integer.class);

    public final EnumPath<com.example.demo.type.RecruitStatus> recruitStatus = createEnum("recruitStatus", com.example.demo.type.RecruitStatus.class);

    public final QSiteUser siteUser;

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final StringPath title = createString("title");

    public QMatching(String variable) {
        this(Matching.class, forVariable(variable), INITS);
    }

    public QMatching(Path<? extends Matching> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatching(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatching(PathMetadata metadata, PathInits inits) {
        this(Matching.class, metadata, inits);
    }

    public QMatching(Class<? extends Matching> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.siteUser = inits.isInitialized("siteUser") ? new QSiteUser(forProperty("siteUser")) : null;
    }

}

