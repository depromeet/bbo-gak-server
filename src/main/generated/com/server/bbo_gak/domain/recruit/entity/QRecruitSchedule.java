package com.server.bbo_gak.domain.recruit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecruitSchedule is a Querydsl query type for RecruitSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruitSchedule extends EntityPathBase<RecruitSchedule> {

    private static final long serialVersionUID = 621202521L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecruitSchedule recruitSchedule = new QRecruitSchedule("recruitSchedule");

    public final DatePath<java.time.LocalDate> deadLine = createDate("deadLine", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecruit recruit;

    public final EnumPath<RecruitScheduleStage> recruitScheduleStage = createEnum("recruitScheduleStage", RecruitScheduleStage.class);

    public QRecruitSchedule(String variable) {
        this(RecruitSchedule.class, forVariable(variable), INITS);
    }

    public QRecruitSchedule(Path<? extends RecruitSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecruitSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecruitSchedule(PathMetadata metadata, PathInits inits) {
        this(RecruitSchedule.class, metadata, inits);
    }

    public QRecruitSchedule(Class<? extends RecruitSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recruit = inits.isInitialized("recruit") ? new QRecruit(forProperty("recruit"), inits.get("recruit")) : null;
    }

}

