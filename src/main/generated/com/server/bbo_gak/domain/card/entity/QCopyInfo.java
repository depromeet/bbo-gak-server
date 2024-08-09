package com.server.bbo_gak.domain.card.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCopyInfo is a Querydsl query type for CopyInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCopyInfo extends EntityPathBase<CopyInfo> {

    private static final long serialVersionUID = 293992271L;

    public static final QCopyInfo copyInfo = new QCopyInfo("copyInfo");

    public final com.server.bbo_gak.global.common.QBaseEntity _super = new com.server.bbo_gak.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> parentCardId = createNumber("parentCardId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QCopyInfo(String variable) {
        super(CopyInfo.class, forVariable(variable));
    }

    public QCopyInfo(Path<? extends CopyInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCopyInfo(PathMetadata metadata) {
        super(CopyInfo.class, metadata);
    }

}

