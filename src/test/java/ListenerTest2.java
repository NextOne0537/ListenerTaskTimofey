import domain.EppState;
import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.parts.EppRegistryElementPart;
import domain.versions.EppRegistryElement;
import domain.versions.EppRegistryProfModule;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import listener.IListenProfModules;
import listener.Listener;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Melton Smith
 * @since 05.12.2020
 */
@RunWith(JUnitParamsRunner.class)
public class ListenerTest2 extends Assert {

    private IListenProfModules listener;

    public static List<TestDataWrapper> wrongDistributionData() {
        return List.of(testCase2());
    }

//    @Test(expected = NullPointerException.class)
//    @Parameters(method = "someData")
//    public void test(TestDataWrapper testDataWrapper){
//        throw new NullPointerException();
//    }


    /**
     * Util
     */
    private static <T extends EppRegistryElement> void createParts(HashMap<T, List<EppRegistryElementPart>> regElemToParts, T regElement, int partsNumber) {
        regElemToParts.put(regElement, new ArrayList<>());

        for (int i = 0; i < partsNumber; i++) {
            EppRegistryElementPart modulePart = new EppRegistryElementPart(regElement);
            regElemToParts.get(regElement).add(modulePart);
        }
    }


    /**
     * I expect "not all element parts are distributed" because of the element with index 7
     */
    public static TestDataWrapper testCase2(){
        List<EppRegistryProfModule> modules = new ArrayList<>() {{
            add(new EppRegistryProfModule("Модуль 1", EppState.Accepted));
            add(new EppRegistryProfModule("Модуль 2", EppState.Accepted));
            add(new EppRegistryProfModule("Модуль 3", EppState.Accepted));
            add(new EppRegistryProfModule("Модуль 4", EppState.Acceptable));
            add(new EppRegistryProfModule("Модуль 5", EppState.Acceptable));
        }};

        //profModules
        HashMap<EppRegistryProfModule, List<EppRegistryElementPart>> module2Parts = new HashMap<>(modules.size());

        createParts(module2Parts, modules.get(0), 4);
        createParts(module2Parts, modules.get(1), 2);
        createParts(module2Parts, modules.get(2), 3);
        createParts(module2Parts, modules.get(3), 2);
//        createParts(module2Parts, modules.get(5), 1);
//        createParts(module2Parts, modules.get(6), 0);


        //elements
        HashMap<EppRegistryElement, List<EppRegistryElementPart>> elem2Parts = new HashMap<>(modules.size());
        List<EppRegistryElement> eppRegistryElements = new ArrayList<>() {{
            add(new EppRegistryElement("Вложенное мероприятие 0", EppState.Forming));
            add(new EppRegistryElement("Вложенное мероприятие 1", EppState.Archived));
            add(new EppRegistryElement("Вложенное мероприятие 2", EppState.Acceptable));
            add(new EppRegistryElement("Вложенное мероприятие 3", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 4", EppState.Forming));
            add(new EppRegistryElement("Вложенное мероприятие 5", EppState.Declined));
            add(new EppRegistryElement("Вложенное мероприятие 6", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 7", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 8", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 9", EppState.Declined));
            add(new EppRegistryElement("Вложенное мероприятие 10", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 11", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 12", EppState.Accepted));
        }};

        //RegElements
        createParts(elem2Parts, eppRegistryElements.get(3), 2);
        createParts(elem2Parts, eppRegistryElements.get(6), 2);
        createParts(elem2Parts, eppRegistryElements.get(7), 4);
        createParts(elem2Parts, eppRegistryElements.get(8), 0);


        //relations
        Collection<MainBond> mainBonds = new ArrayList<>(modules.size());
        HashMap<EppRegistryProfModule, Collection<EppRegistryElement>> moduleToRegElements = new HashMap<>(modules.size());
        moduleToRegElements.put(modules.get(0), List.of(eppRegistryElements.get(3), eppRegistryElements.get(6), eppRegistryElements.get(7), eppRegistryElements.get(8)));
//        moduleToRegElements.put(modules.get(1), eppRegistryElements.subList(4, 8));
//        moduleToRegElements.put(modules.get(2), eppRegistryElements.subList(4, 8));
//        moduleToRegElements.put(modules.get(3), eppRegistryElements.subList(4, 8));
//        moduleToRegElements.put(modules.get(4), eppRegistryElements.subList(4, 8));
//        moduleToRegElements.put(modules.get(5), eppRegistryElements.subList(4, 8));
//        moduleToRegElements.put(modules.get(6),  eppRegistryElements.subList(4, 8));


        //module to its bonds
        HashMap<EppRegistryProfModule, Collection<MainBond>> profModuleToBonds = new HashMap<>();

        moduleToRegElements.forEach((profModule, collection) ->{
            Collection<MainBond> mainBondsForModule = generateMainBonds(profModule, collection);
            profModuleToBonds.put(profModule, mainBondsForModule);
            mainBonds.addAll(mainBondsForModule);
        });

        List<Part2PartBond> parts2Parts = new ArrayList<>();
        parts2Parts.addAll(createPart2Part(modules.get(0), eppRegistryElements.get(3), module2Parts, elem2Parts, profModuleToBonds, "1/2", "2/1"));
        parts2Parts.addAll(createPart2Part(modules.get(0), eppRegistryElements.get(6), module2Parts, elem2Parts, profModuleToBonds, "1/2", "2/3"));


        return new TestDataWrapper(module2Parts, elem2Parts, parts2Parts, mainBonds);
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
    private static List<Part2PartBond> createPart2Part(EppRegistryProfModule profModule,  EppRegistryElement registryElement,
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

    /**
     *
     * @param parts regElemPart - modulePart, example (1/2, 2/3)
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
     * do not use twice for a single profModule
     */
    private static Collection<MainBond> generateMainBonds(@NotNull EppRegistryProfModule module, @NotNull Collection<EppRegistryElement> eppRegistryElements){
        AtomicInteger atomicInteger = new AtomicInteger(1);

        return eppRegistryElements
                .stream()
                .map(regElement -> new MainBond(atomicInteger.getAndIncrement(), module, regElement))
                .collect(Collectors.toList());
    }

    @Test
    @Parameters(method = "wrongDistributionData")
    public void test1(TestDataWrapper testDataWrapper){
        var illegalStateException = assertThrows(IllegalStateException.class, () -> listener.onEvent(testDataWrapper.getProfModules(), testDataWrapper.getRegistryElements(), testDataWrapper.getMainBonds(), testDataWrapper.getPart2PartBonds()));
        assertEquals("Нельзя согласовать на согласование профмодуль Модуль 1, т.к. распределены не все части вложенных элементов.", illegalStateException.getMessage());
    }

    @Before
    public void getListener(){
        listener = Listener.INSTANCE;
    }

    @After
    public void endTest() {
        listener = null;
    }
}
