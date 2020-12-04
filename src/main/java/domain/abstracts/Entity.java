package domain.abstracts;

import services.IdGenerator;

import javax.validation.constraints.NotNull;

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

    public void setTitle(String title) {
        this.title = title;
    }
}
