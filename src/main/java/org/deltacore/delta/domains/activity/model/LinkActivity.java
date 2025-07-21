package org.deltacore.delta.domains.activity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class LinkActivity {
    @Column(nullable = false, length = 500)
    private String link;

    @Column
    private String description;
}
