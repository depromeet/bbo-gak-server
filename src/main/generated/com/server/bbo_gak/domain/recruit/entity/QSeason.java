package com.server.bbo_gak.domain.recruit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSeason is a Querydsl query type for Season
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeason extends EntityPathBase<Season> {

    private static final long serialVersionUID = 1963900415L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSeason season = new QSeason("season");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.server.bbo_gak.domain.user.entity.QUser user;

    public QSeason(String variable) {
        this(Season.class, forVariable(variable), INITS);
    }

    public QSeason(Path<? extends Season> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSeason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSeason(PathMetadata metadata, PathInits inits) {
        this(Season.class, metadata, inits);
    }

    public QSeason(Class<? extends Season> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.server.bbo_gak.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

