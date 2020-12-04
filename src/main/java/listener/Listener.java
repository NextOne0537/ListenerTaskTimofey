package listener;

import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.versions.EppRegistryProfModule;

import java.util.Collection;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class Listener implements IListenProfModules {

    public static IListenProfModules INSTANCE = new Listener();

    private Listener() {}

    @Override
    //TODO implement me
    public boolean onEvent(Collection<EppRegistryProfModule> profModules, Collection<MainBond> mainBonds, Collection<Part2PartBond> part2PartBonds) {
        throw new NullPointerException();
    }
}
