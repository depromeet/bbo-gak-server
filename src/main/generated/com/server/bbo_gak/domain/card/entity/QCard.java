package com.server.bbo_gak.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCard is a Querydsl query type for Card
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCard extends EntityPathBase<Card> {

    private static final long serialVersionUID = -1695526244L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCard card = new QCard("card");

    public final com.server.bbo_gak.global.common.QBaseEntity _super = new com.server.bbo_gak.global.common.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> accessTime = createDateTime("accessTime", java.time.LocalDateTime.class);

    public final ListPath<CardImage, QCardImage> cardImageList = this.<CardImage, QCardImage>createList("cardImageList", CardImage.class, QCardImage.class, PathInits.DIRECT2);

    public final ListPath<CardMemo, QCardMemo> cardMemoList = this.<CardMemo, QCardMemo>createList("cardMemoList", CardMemo.class, QCardMemo.class, PathInits.DIRECT2);

    public final ListPath<CardTag, QCardTag> cardTagList = this.<CardTag, QCardTag>createList("cardTagList", CardTag.class, QCardTag.class, PathInits.DIRECT2);

    public final ListPath<CardType, QCardType> cardTypeList = this.<CardType, QCardType>createList("cardTypeList", CardType.class, QCardType.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final BooleanPath copyFlag = createBoolean("copyFlag");

    public final QCopyInfo copyInfo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.server.bbo_gak.domain.recruit.entity.QRecruit recruit;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public final com.server.bbo_gak.domain.user.entity.QUser user;

    public QCard(String variable) {
        this(Card.class, forVariable(variable), INITS);
    }

    public QCard(Path<? extends Card> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCard(PathMetadata metadata, PathInits inits) {
        this(Card.class, metadata, inits);
    }

    public QCard(Class<? extends Card> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.copyInfo = inits.isInitialized("copyInfo") ? new QCopyInfo(forProperty("copyInfo")) : null;
        this.recruit = inits.isInitialized("recruit") ? new com.server.bbo_gak.domain.recruit.entity.QRecruit(forProperty("recruit"), inits.get("recruit")) : null;
        this.user = inits.isInitialized("user") ? new com.server.bbo_gak.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

