package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Progress extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ProgressType type;
    private Float progressCounter;
}
