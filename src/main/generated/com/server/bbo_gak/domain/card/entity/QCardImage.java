package com.server.bbo_gak.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCardImage is a Querydsl query type for CardImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCardImage extends EntityPathBase<CardImage> {

    private static final long serialVersionUID = -1226921953L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCardImage cardImage = new QCardImage("cardImage");

    public final QCard card;

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.server.bbo_gak.global.constant.FileExtension> imageFileExtension = createEnum("imageFileExtension", com.server.bbo_gak.global.constant.FileExtension.class);

    public final StringPath imageKey = createString("imageKey");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath TYPE = createString("TYPE");

    public QCardImage(String variable) {
        this(CardImage.class, forVariable(variable), INITS);
    }

    public QCardImage(Path<? extends CardImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCardImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCardImage(PathMetadata metadata, PathInits inits) {
        this(CardImage.class, metadata, inits);
    }

    public QCardImage(Class<? extends CardImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.card = inits.isInitialized("card") ? new QCard(forProperty("card"), inits.get("card")) : null;
    }

}

