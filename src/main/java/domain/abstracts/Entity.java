package domain.abstracts;

import services.IdGenerator;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public abstract class Entity {

    @NotNull
    protected final Long id;

    protected String title;

    public Entity() {
        this.id = IdGenerator.INSTANCE.generateId();
    }

    public Entity(String title) {
        this();
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        else if (!(obj instanceof Entity))
            return false;

        return this.id.equals(((Entity) obj).id);
    }
}
