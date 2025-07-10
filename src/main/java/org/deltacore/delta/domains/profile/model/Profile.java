package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.tutoring.model.Feedback;
import org.deltacore.delta.shared.model.GeneralData;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(name = "profile_image", length = 1000)
    private String profileImage;

    @Column(length = 15, unique = true)
    private String phoneNumber;

    @Column(scale = 4, precision = 19)
    private BigDecimal totalScore;

    @Column(nullable = false)
    private Integer level;

    @Column(length = 150)
    private String bio;
}
