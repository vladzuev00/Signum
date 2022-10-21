package by.aurorasoft.signum.crud.model.entity;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;

import java.util.Objects;

public abstract class BaseEntity<IdType> implements AbstractEntity<IdType> {

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (this.getClass() != otherObject.getClass()) {
            return false;
        }
        final BaseEntity<IdType> other = (BaseEntity<IdType>) otherObject;
        return Objects.equals(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[id = " + this.getId() + "]";
    }
}
