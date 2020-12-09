package domain.parts;

import domain.abstracts.Entity;
import domain.versions.EppRegistryElement;
import services.PartNumberGenerator;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class EppRegistryElementPart extends Entity {

    private int number;
    private EppRegistryElement parent;

    public EppRegistryElementPart(EppRegistryElement parent) {
        super();
        int partNumber = PartNumberGenerator.INSTANCE.getPartNumber(parent.getId());
        this.title = "Часть " + parent.getTitle() + " №" + partNumber;
        this.number = partNumber;
        this.parent = parent;

    }

    public int getNumber() {
        return number;
    }

    public EppRegistryElement getParent() { return parent; }
}
