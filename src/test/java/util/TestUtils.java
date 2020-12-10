package util;

import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.parts.EppRegistryElementPart;
import domain.versions.EppRegistryElement;
import domain.versions.EppRegistryProfModule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Static methods designed to help you build your test data
 *
 * @author Melton Smith
 * @since 10.12.2020
 */
public class TestUtils {

    /**
     * Util
     * Creates parts for a given <T> reg element
     * @param partsNumber - how many parts you want to create
     */
    public static <T extends EppRegistryElement> void createParts(HashMap<T, List<EppRegistryElementPart>> regElemToParts, T regElement, int partsNumber) {
        regElemToParts.put(regElement, new ArrayList<>());

        for (int i = 0; i < partsNumber; i++) {
            EppRegistryElementPart modulePart = new EppRegistryElementPart(regElement);
            regElemToParts.get(regElement).add(modulePart);
        }
    }


    /**
     * Creates part2parts bonds
     * @param parts regElemPart - modulePart, example (1/2, 2/3)
     *
     */
    private static List<Part2PartBond> createDistribution (List<EppRegistryElementPart> moduleParts,
                                                           List<EppRegistryElementPart> elementParts,
                                                           MainBond mainBond,
                                                           String... parts){
        List<Part2PartBond> result = new ArrayList<>();

        Arrays.stream(parts)
                .forEach(string -> {
                    String[] split = string.split("/");
                    int regElemPartNumber = Integer.parseInt(split[0]);
                    int modulePartNumber = Integer.parseInt(split[1]);

                    Optional<EppRegistryElementPart> modulePart = moduleParts.stream()
                            .filter(part -> part.getNumber() == modulePartNumber)
                            .findFirst();
                    Optional<EppRegistryElementPart> registryElementPart = elementParts.stream()
                            .filter(part -> part.getNumber() == regElemPartNumber)
                            .findFirst();
                    if (registryElementPart.isPresent() && modulePart.isPresent())
                        result.add(new Part2PartBond(mainBond, modulePart.get(), registryElementPart.get()));

                });

        return result;
    }


    /**
     * Util
     * Creates main=Bonds between a module and passed eppRegistryElements
     *
     * NOTE do not use twice for a single profModule
     */
    public static Collection<MainBond> createMainBonds(EppRegistryProfModule module, Collection<EppRegistryElement> eppRegistryElements){
        AtomicInteger atomicInteger = new AtomicInteger(1);

        return eppRegistryElements
                .stream()
                .map(regElement -> new MainBond(atomicInteger.getAndIncrement(), module, regElement))
                .collect(Collectors.toList());
    }

    /**
     * @param profModule a profModule for which we want to create part2part
     * @param registryElement a registryElement for which we want to create part2part
     * @param module2Parts modules to their parts
     * @param elem2Parts elements to their parts
     * @param profModuleToBonds modules to their mainBonds
     * @param parts a string array which represents parts distribution (elemPartNumber/modulePartNumber).
     *              For example: (1/2, 2/3) means that I want to create part2part bond between: elemPart #1 and modulePart #2
     *                                                                                          elemPart #2 and modulePart #3
     * @return partBonds created
     */
    public static List<Part2PartBond> createPart2Part(EppRegistryProfModule profModule,  EppRegistryElement registryElement,
                                                       HashMap<EppRegistryProfModule, List<EppRegistryElementPart>> module2Parts,
                                                       HashMap<EppRegistryElement, List<EppRegistryElementPart>> elem2Parts,
                                                       HashMap<EppRegistryProfModule, Collection<MainBond>> profModuleToBonds,
                                                       String... parts) {
        List<EppRegistryElementPart> moduleParts = module2Parts.get(profModule);
        List<EppRegistryElementPart> regElemParts = elem2Parts.get(registryElement);
        Optional<MainBond> mainBondOpt = profModuleToBonds.get(profModule)
                .stream()
                .filter(mainBond -> mainBond.getElement().equals(registryElement))
                .findFirst();

        return mainBondOpt
                .map(mainBond -> createDistribution(moduleParts, regElemParts, mainBond, parts))
                .orElse(Collections.emptyList());
    }
}
