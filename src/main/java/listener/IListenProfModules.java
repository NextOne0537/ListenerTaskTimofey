package listener;

import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.versions.EppRegistryProfModule;

import java.util.Collection;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public interface IListenProfModules {
    
    boolean onEvent(Collection<EppRegistryProfModule> profModules, Collection<MainBond> mainBonds, Collection<Part2PartBond> part2PartBonds);
}
