package com.server.bbo_gak.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardType is a Querydsl query type for CardType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardType extends EntityPathBase<CardType> {

    private static final long serialVersionUID = 792045558L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCardType cardType = new QCardType("cardType");

    public final com.server.bbo_gak.global.common.QBaseEntity _super = new com.server.bbo_gak.global.common.QBaseEntity(this);

    public final QCard card;

    public final EnumPath<CardTypeValue> cardTypeValue = createEnum("cardTypeValue", CardTypeValue.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QCardType(String variable) {
        this(CardType.class, forVariable(variable), INITS);
    }

    public QCardType(Path<? extends CardType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCardType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCardType(PathMetadata metadata, PathInits inits) {
        this(CardType.class, metadata, inits);
    }

    public QCardType(Class<? extends CardType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.card = inits.isInitialized("card") ? new QCard(forProperty("card"), inits.get("card")) : null;
    }

}

