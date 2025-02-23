package com.babpat.server.member.entity;

import com.babpat.server.common.model.BaseEntity;
import com.babpat.server.common.enums.BaseStatus;
import com.babpat.server.member.entity.enums.Track;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Track track;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVATE'")
    private BaseStatus baseStatus;
}
