package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPenaltyScore is a Querydsl query type for PenaltyScore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPenaltyScore extends EntityPathBase<PenaltyScore> {

    private static final long serialVersionUID = -1825667558L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPenaltyScore penaltyScore = new QPenaltyScore("penaltyScore");

    public final EnumPath<com.example.demo.type.PenaltyCode> code = createEnum("code", com.example.demo.type.PenaltyCode.class);

    public final DateTimePath<java.sql.Timestamp> createTime = createDateTime("createTime", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final QSiteUser siteUser;

    public QPenaltyScore(String variable) {
        this(PenaltyScore.class, forVariable(variable), INITS);
    }

    public QPenaltyScore(Path<? extends PenaltyScore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPenaltyScore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPenaltyScore(PathMetadata metadata, PathInits inits) {
        this(PenaltyScore.class, metadata, inits);
    }

    public QPenaltyScore(Class<? extends PenaltyScore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.siteUser = inits.isInitialized("siteUser") ? new QSiteUser(forProperty("siteUser")) : null;
    }

}

