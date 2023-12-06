package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMannerScore is a Querydsl query type for MannerScore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMannerScore extends EntityPathBase<MannerScore> {

    private static final long serialVersionUID = 1133871200L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMannerScore mannerScore = new QMannerScore("mannerScore");

    public final DateTimePath<java.sql.Timestamp> createTime = createDateTime("createTime", java.sql.Timestamp.class);

    public final StringPath id = createString("id");

    public final QMatching matching;

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final QSiteUser siteUser;

    public QMannerScore(String variable) {
        this(MannerScore.class, forVariable(variable), INITS);
    }

    public QMannerScore(Path<? extends MannerScore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMannerScore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMannerScore(PathMetadata metadata, PathInits inits) {
        this(MannerScore.class, metadata, inits);
    }

    public QMannerScore(Class<? extends MannerScore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.matching = inits.isInitialized("matching") ? new QMatching(forProperty("matching"), inits.get("matching")) : null;
        this.siteUser = inits.isInitialized("siteUser") ? new QSiteUser(forProperty("siteUser")) : null;
    }

}

