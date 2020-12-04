package domain.bonds;

import annotations.ManyToOne;
import domain.abstracts.Entity;
import domain.parts.EppRegistryElementPart;

import javax.validation.constraints.NotNull;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class Part2PartBond extends Entity {

    @NotNull
    @ManyToOne
    private MainBond mainBond;
    @NotNull
    private EppRegistryElementPart modulePart;
    @NotNull
    private EppRegistryElementPart elementPart;

    public Part2PartBond(@NotNull MainBond mainBond, @NotNull EppRegistryElementPart modulePart, @NotNull EppRegistryElementPart elementPart) {
        super();
        this.mainBond = mainBond;
        this.modulePart = modulePart;
        this.elementPart = elementPart;
    }
}
