package domain.versions;

import domain.EppState;
import domain.abstracts.StateObject;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class EppRegistryElement extends StateObject {
    public EppRegistryElement(String title, EppState state) {
        super(title, state);
    }
}
