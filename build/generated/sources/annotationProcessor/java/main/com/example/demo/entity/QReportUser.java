package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportUser is a Querydsl query type for ReportUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportUser extends EntityPathBase<ReportUser> {

    private static final long serialVersionUID = -1350373904L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportUser reportUser = new QReportUser("reportUser");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSiteUser reportedUser;

    public final QSiteUser reportingUser;

    public final StringPath title = createString("title");

    public QReportUser(String variable) {
        this(ReportUser.class, forVariable(variable), INITS);
    }

    public QReportUser(Path<? extends ReportUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportUser(PathMetadata metadata, PathInits inits) {
        this(ReportUser.class, metadata, inits);
    }

    public QReportUser(Class<? extends ReportUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportedUser = inits.isInitialized("reportedUser") ? new QSiteUser(forProperty("reportedUser")) : null;
        this.reportingUser = inits.isInitialized("reportingUser") ? new QSiteUser(forProperty("reportingUser")) : null;
    }

}

