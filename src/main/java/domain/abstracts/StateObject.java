package domain.abstracts;

import domain.EppState;

/**
 * @author Melton Smith
 * @since 03.12.2020
 */
public abstract class StateObject extends Entity{

    private EppState state;

    public StateObject(String title, EppState state) {
        super(title);
        this.state = state;
    }

    public EppState getState() {
        return state;
    }
}
