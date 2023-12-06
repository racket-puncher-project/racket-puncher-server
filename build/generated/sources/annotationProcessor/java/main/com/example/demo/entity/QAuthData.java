package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthData is a Querydsl query type for AuthData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthData extends EntityPathBase<AuthData> {

    private static final long serialVersionUID = -1391746941L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthData authData = new QAuthData("authData");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final QSiteUser siteUser;

    public QAuthData(String variable) {
        this(AuthData.class, forVariable(variable), INITS);
    }

    public QAuthData(Path<? extends AuthData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthData(PathMetadata metadata, PathInits inits) {
        this(AuthData.class, metadata, inits);
    }

    public QAuthData(Class<? extends AuthData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.siteUser = inits.isInitialized("siteUser") ? new QSiteUser(forProperty("siteUser")) : null;
    }

}

