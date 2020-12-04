package domain.bonds;

import domain.abstracts.Entity;
import domain.versions.EppRegistryElement;
import domain.versions.EppRegistryProfModule;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class MainBond extends Entity {

    @NotNull
    private final int priority;
    @NotNull
    private EppRegistryProfModule module;
    @NotNull
    private EppRegistryElement element;

    public MainBond(@NotNull int priority, @NotNull EppRegistryProfModule module, @NotNull EppRegistryElement element) {
        super();
        this.priority = priority;
        this.module = module;
        this.element = element;
    }

    public int getPriority() {
        return priority;
    }

    public EppRegistryProfModule getModule() {
        return module;
    }

    public EppRegistryElement getElement() {
        return element;
    }
}
