package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSiteUser is a Querydsl query type for SiteUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSiteUser extends EntityPathBase<SiteUser> {

    private static final long serialVersionUID = 2146678467L;

    public static final QSiteUser siteUser = new QSiteUser("siteUser");

    public final StringPath address = createString("address");

    public final EnumPath<com.example.demo.type.AgeGroup> ageGroup = createEnum("ageGroup", com.example.demo.type.AgeGroup.class);

    public final ListPath<Apply, QApply> applies = this.<Apply, QApply>createList("applies", Apply.class, QApply.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final EnumPath<com.example.demo.type.GenderType> gender = createEnum("gender", com.example.demo.type.GenderType.class);

    public final ListPath<Matching, QMatching> hostedMatches = this.<Matching, QMatching>createList("hostedMatches", Matching.class, QMatching.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPhoneVerified = createBoolean("isPhoneVerified");

    public final NumberPath<Double> mannerScore = createNumber("mannerScore", Double.class);

    public final StringPath nickname = createString("nickname");

    public final ListPath<Notification, QNotification> notifications = this.<Notification, QNotification>createList("notifications", Notification.class, QNotification.class, PathInits.DIRECT2);

    public final EnumPath<com.example.demo.type.Ntrp> ntrp = createEnum("ntrp", com.example.demo.type.Ntrp.class);

    public final StringPath password = createString("password");

    public final NumberPath<Integer> penaltyScore = createNumber("penaltyScore", Integer.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profileImg = createString("profileImg");

    public final ListPath<String, StringPath> roles = this.<String, StringPath>createList("roles", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath siteusername = createString("siteusername");

    public final StringPath zipCode = createString("zipCode");

    public QSiteUser(String variable) {
        super(SiteUser.class, forVariable(variable));
    }

    public QSiteUser(Path<? extends SiteUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSiteUser(PathMetadata metadata) {
        super(SiteUser.class, metadata);
    }

}

