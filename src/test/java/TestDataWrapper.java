import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.versions.EppRegistryProfModule;

import java.util.Collection;

/**
 * @author Melton Smith
 * @since 03.12.2020
 */
public class TestDataWrapper {
    private final Collection<EppRegistryProfModule> profModules;
    private final Collection<Part2PartBond> part2PartBonds;
    private final Collection<MainBond> mainBonds;

    public TestDataWrapper(Collection<EppRegistryProfModule> profModules, Collection<Part2PartBond> part2PartBonds, Collection<MainBond> mainBonds) {
        this.profModules = profModules;
        this.part2PartBonds = part2PartBonds;
        this.mainBonds = mainBonds;
    }

    public Collection<EppRegistryProfModule> getProfModules() {
        return profModules;
    }

    public Collection<Part2PartBond> getPart2PartBonds() {
        return part2PartBonds;
    }

    public Collection<MainBond> getMainBonds() {
        return mainBonds;
    }
}
