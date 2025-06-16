package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Achievement extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private AchievementCondition condition;
    private String urlImage;
    private String description;
}
