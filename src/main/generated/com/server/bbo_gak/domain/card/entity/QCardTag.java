package com.server.bbo_gak.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardTag is a Querydsl query type for CardTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardTag extends EntityPathBase<CardTag> {

    private static final long serialVersionUID = 1688117086L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCardTag cardTag = new QCardTag("cardTag");

    public final com.server.bbo_gak.global.common.QBaseEntity _super = new com.server.bbo_gak.global.common.QBaseEntity(this);

    public final QCard card;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTag tag;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QCardTag(String variable) {
        this(CardTag.class, forVariable(variable), INITS);
    }

    public QCardTag(Path<? extends CardTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCardTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCardTag(PathMetadata metadata, PathInits inits) {
        this(CardTag.class, metadata, inits);
    }

    public QCardTag(Class<? extends CardTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.card = inits.isInitialized("card") ? new QCard(forProperty("card"), inits.get("card")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag")) : null;
    }

}

