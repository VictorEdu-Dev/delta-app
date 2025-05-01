package org.deltacore.delta.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class GeneralData implements Serializable {
    @Version
    protected LocalDateTime version;
}
