package org.deltacore.delta.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.model.Feedback;
import org.deltacore.delta.model.GeneralData;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Lob
    @Column(name = "profile_image", columnDefinition = "BYTEA")
    private byte[] profileImage;

    @Column(length = 15, unique = true)
    private String phoneNumber;

    @Column(scale = 4, precision = 19)
    private BigDecimal totalScore;

    @Column(nullable = false)
    private Integer level;

    @Column(length = 150)
    private String bio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "profile")
    private List<Feedback> feedbacks;
}
