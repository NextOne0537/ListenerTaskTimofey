import domain.EppState;
import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.parts.EppRegistryElementPart;
import domain.versions.EppRegistryElement;
import domain.versions.EppRegistryProfModule;
import listener.IListenProfModules;
import listener.Listener;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
@Deprecated
@RunWith(Parameterized.class)
public class ListenerTest extends Assert {

    private Collection<TestDataWrapper> testCases;
    private IListenProfModules listener;

    public ListenerTest(Collection<TestDataWrapper> testCases) {
        this.testCases = testCases;
    }

    @Parameters
    public static Collection<Object[]> data(){
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[] {List.of(testCase1())});
        return params;
    }

    @Test
    public void onEventTest(){
        testCases.forEach(testCase ->{
            assertTrue(listener.onEvent(testCase.getProfModules(), testCase.getRegistryElements(), testCase.getMainBonds(), testCase.getPart2PartBonds()));
        });
    }


    public static TestDataWrapper testCase1(){
        List<EppRegistryProfModule> modules = new ArrayList<>() {{
            add(new EppRegistryProfModule("Модуль 1", EppState.Forming));
            add(new EppRegistryProfModule("Модуль 2", EppState.Archived));
            add(new EppRegistryProfModule("Модуль 3", EppState.Accepted));
            add(new EppRegistryProfModule("Модуль 4", EppState.Declined));
            add(new EppRegistryProfModule("Модуль 5", EppState.Acceptable));
        }};

        //profModules
        HashMap<EppRegistryProfModule, List<EppRegistryElementPart>> module2Parts = new HashMap<>(modules.size());
        modules.forEach(module -> createParts(module2Parts, module));

        //elements
        HashMap<EppRegistryElement, List<EppRegistryElementPart>> elem2Parts = new HashMap<>(modules.size());
        List<EppRegistryElement> eppRegistryElements = new ArrayList<>() {{
            add(new EppRegistryElement("Вложенное мероприятие 1", EppState.Forming));
            add(new EppRegistryElement("Вложенное мероприятие 2", EppState.Archived));
            add(new EppRegistryElement("Вложенное мероприятие 3", EppState.Acceptable));
            add(new EppRegistryElement("Вложенное мероприятие 4", EppState.Acceptable));
            add(new EppRegistryElement("Вложенное мероприятие 5", EppState.Forming));
            add(new EppRegistryElement("Вложенное мероприятие 6", EppState.Declined));
            add(new EppRegistryElement("Вложенное мероприятие 7", EppState.Acceptable));
            add(new EppRegistryElement("Вложенное мероприятие 8", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 9", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 10", EppState.Declined));
            add(new EppRegistryElement("Вложенное мероприятие 11", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 12", EppState.Accepted));
            add(new EppRegistryElement("Вложенное мероприятие 13", EppState.Accepted));
        }};

        //RegElements
        eppRegistryElements.forEach(eppRegistryElement -> createParts(elem2Parts, eppRegistryElement));

        //relations
        Collection<MainBond> mainBonds = new ArrayList<>(modules.size());
        HashMap<EppRegistryProfModule, Collection<EppRegistryElement>> moduleToRegElements = new HashMap<>(modules.size());
        moduleToRegElements.put(modules.get(0), eppRegistryElements.subList(0, 3));
        moduleToRegElements.put(modules.get(1), eppRegistryElements.subList(3, 6));
        moduleToRegElements.put(modules.get(2), eppRegistryElements.subList(6, 9));
        moduleToRegElements.put(modules.get(3), eppRegistryElements.subList(9, 13));
        moduleToRegElements.put(modules.get(4),  Collections.emptyList());

        moduleToRegElements.forEach((id, collection) ->{
            mainBonds.addAll(generateMainBonds(id, collection));
        });

        //part2part relations (correct distributing)
        Collection<Part2PartBond> parts2Parts = createParts2Parts(module2Parts, elem2Parts, mainBonds);

        return new TestDataWrapper(module2Parts, elem2Parts, parts2Parts, mainBonds);
    }

    /**
     * Util
     */
    private static Collection<Part2PartBond> createParts2Parts(HashMap<EppRegistryProfModule, List<EppRegistryElementPart>> moduleId2Parts,
                                                               HashMap<EppRegistryElement, List<EppRegistryElementPart>> elemId2Parts,
                                                               Collection<MainBond> mainBonds) {
        List<Part2PartBond> part2PartBonds = new ArrayList<>();
        mainBonds.forEach(mainBond ->{
            EppRegistryProfModule module = mainBond.getModule();
            EppRegistryElement element = mainBond.getElement();

            var eppRegistryElementParts = elemId2Parts.getOrDefault(element, Collections.emptyList());
            var moduleParts = moduleId2Parts.getOrDefault(module, Collections.emptyList());

            for (int i = 0; i < moduleParts.size(); i++) {
                var modulePart = moduleParts.get(i);
                if (i > eppRegistryElementParts.size()-1)
                    continue;

                var eppRegistryElementPart = eppRegistryElementParts.get(i);
                part2PartBonds.add(new Part2PartBond(mainBond, modulePart, eppRegistryElementPart));

            }
        });
        return part2PartBonds;
    }

    /**
     * Util
     */
    private static <T extends EppRegistryElement> void createParts(HashMap<T, List<EppRegistryElementPart>> regElemToParts, T regElement) {
        Random rand = new Random();
        int partsNumber = rand.nextInt(4);  //количество частей на каждый элемент реестра (проф модуль)
        regElemToParts.put(regElement, new ArrayList<>());

        for (int i = 0; i < partsNumber; i++) {
            EppRegistryElementPart modulePart = new EppRegistryElementPart(regElement);
            regElemToParts.get(regElement).add(modulePart);
        }
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

    @Before
    public void getListener(){
        listener = Listener.INSTANCE;
    }

    @After
    public void endTest() {
        listener = null;
        testCases = null;
    }

}
