package com.server.bbo_gak.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardMemo is a Querydsl query type for CardMemo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardMemo extends EntityPathBase<CardMemo> {

    private static final long serialVersionUID = 791817718L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCardMemo cardMemo = new QCardMemo("cardMemo");

    public final com.server.bbo_gak.global.common.QBaseEntity _super = new com.server.bbo_gak.global.common.QBaseEntity(this);

    public final QCard card;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QCardMemo(String variable) {
        this(CardMemo.class, forVariable(variable), INITS);
    }

    public QCardMemo(Path<? extends CardMemo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCardMemo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCardMemo(PathMetadata metadata, PathInits inits) {
        this(CardMemo.class, metadata, inits);
    }

    public QCardMemo(Class<? extends CardMemo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.card = inits.isInitialized("card") ? new QCard(forProperty("card"), inits.get("card")) : null;
    }

}

