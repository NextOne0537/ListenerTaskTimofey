import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.parts.EppRegistryElementPart;
import domain.versions.EppRegistryElement;
import domain.versions.EppRegistryProfModule;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Melton Smith
 * @since 03.12.2020
 */
public class TestDataWrapper {
    private final Map<EppRegistryProfModule, List<EppRegistryElementPart>> profModules;
    private final Map<EppRegistryElement, List<EppRegistryElementPart>> registryElements;
    private final Collection<Part2PartBond> part2PartBonds;
    private final Collection<MainBond> mainBonds;


    public TestDataWrapper(Map<EppRegistryProfModule, List<EppRegistryElementPart>> profModules,
                           Map<EppRegistryElement, List<EppRegistryElementPart>> registryElements,
                           Collection<Part2PartBond> part2PartBonds,
                           Collection<MainBond> mainBonds) {
        this.profModules = profModules;
        this.registryElements = registryElements;
        this.part2PartBonds = part2PartBonds;
        this.mainBonds = mainBonds;
    }

    public Map<EppRegistryElement, List<EppRegistryElementPart>> getRegistryElements() {
        return registryElements;
    }

    public Map<EppRegistryProfModule, List<EppRegistryElementPart>> getProfModules() {
        return profModules;
    }

    public Collection<Part2PartBond> getPart2PartBonds() {
        return part2PartBonds;
    }

    public Collection<MainBond> getMainBonds() {
        return mainBonds;
    }
}
