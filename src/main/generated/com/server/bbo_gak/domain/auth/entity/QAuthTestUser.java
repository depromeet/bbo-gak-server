package com.server.bbo_gak.domain.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthTestUser is a Querydsl query type for AuthTestUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthTestUser extends EntityPathBase<AuthTestUser> {

    private static final long serialVersionUID = -1893869719L;

    public static final QAuthTestUser authTestUser = new QAuthTestUser("authTestUser");

    public final com.server.bbo_gak.domain.user.entity.QUser _super = new com.server.bbo_gak.domain.user.entity.QUser(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath loginId = createString("loginId");

    //inherited
    public final StringPath name = _super.name;

    public final StringPath password = createString("password");

    //inherited
    public final EnumPath<com.server.bbo_gak.domain.user.entity.UserRole> role = _super.role;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QAuthTestUser(String variable) {
        super(AuthTestUser.class, forVariable(variable));
    }

    public QAuthTestUser(Path<? extends AuthTestUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthTestUser(PathMetadata metadata) {
        super(AuthTestUser.class, metadata);
    }

}

