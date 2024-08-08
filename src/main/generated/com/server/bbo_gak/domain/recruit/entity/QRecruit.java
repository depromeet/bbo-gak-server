package com.server.bbo_gak.domain.recruit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecruit is a Querydsl query type for Recruit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruit extends EntityPathBase<Recruit> {

    private static final long serialVersionUID = -134309982L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecruit recruit = new QRecruit("recruit");

    public final com.server.bbo_gak.global.common.QBaseEntity _super = new com.server.bbo_gak.global.common.QBaseEntity(this);

    public final ListPath<com.server.bbo_gak.domain.card.entity.Card, com.server.bbo_gak.domain.card.entity.QCard> cardList = this.<com.server.bbo_gak.domain.card.entity.Card, com.server.bbo_gak.domain.card.entity.QCard>createList("cardList", com.server.bbo_gak.domain.card.entity.Card.class, com.server.bbo_gak.domain.card.entity.QCard.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<RecruitStatus> recruitStatus = createEnum("recruitStatus", RecruitStatus.class);

    public final ListPath<RecruitSchedule, QRecruitSchedule> scheduleList = this.<RecruitSchedule, QRecruitSchedule>createList("scheduleList", RecruitSchedule.class, QRecruitSchedule.class, PathInits.DIRECT2);

    public final QSeason season;

    public final StringPath siteUrl = createString("siteUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public final com.server.bbo_gak.domain.user.entity.QUser user;

    public QRecruit(String variable) {
        this(Recruit.class, forVariable(variable), INITS);
    }

    public QRecruit(Path<? extends Recruit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecruit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecruit(PathMetadata metadata, PathInits inits) {
        this(Recruit.class, metadata, inits);
    }

    public QRecruit(Class<? extends Recruit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.season = inits.isInitialized("season") ? new QSeason(forProperty("season"), inits.get("season")) : null;
        this.user = inits.isInitialized("user") ? new com.server.bbo_gak.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

