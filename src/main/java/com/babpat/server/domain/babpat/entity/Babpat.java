package com.babpat.server.domain.babpat.entity;

import com.babpat.server.domain.babpat.entity.enums.MealSpeed;
import com.babpat.server.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Table(name = "babpat")
public class Babpat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "babpat_id")
    private Long id;

    @NotNull
    private Long leaderId;

    @NotNull
    private Long restaurantId;

    @NotNull
    private String comment;

    @NotNull
    private Integer headCount;

    @NotNull
    private LocalDate patDate;

    @NotNull
    private LocalTime patTime;

    @Enumerated(EnumType.STRING)
    private MealSpeed mealSpeed;
}
