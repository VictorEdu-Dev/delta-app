package org.deltacore.delta.domains.tutoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.shared.model.GeneralData;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subject extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 10)
    private String code;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
    @ToString.Exclude
    private List<Activity> activity;

    @ToString.Exclude
    @OneToOne(mappedBy = "subject", cascade = CascadeType.DETACH)
    @Fetch(FetchMode.SELECT)
    private Tutoring tutoring;
}
