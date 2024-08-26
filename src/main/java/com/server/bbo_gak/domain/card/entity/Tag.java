package com.server.bbo_gak.domain.card.entity;

import com.server.bbo_gak.domain.user.entity.Job;
import com.server.bbo_gak.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE tag SET deleted = true WHERE tag_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    @Enumerated(EnumType.STRING)
    private Job job;

    private String name;
}
