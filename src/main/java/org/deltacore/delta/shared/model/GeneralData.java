package org.deltacore.delta.shared.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class GeneralData implements Serializable {
    public abstract Long getId();

    @Version
    protected LocalDateTime version;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> oClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();

        if (!thisClass.equals(oClass)) return false;

        GeneralData that = (GeneralData) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return getId() != null ? getId().hashCode() :
                (this instanceof HibernateProxy
                        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                        : getClass().hashCode());
    }
}
