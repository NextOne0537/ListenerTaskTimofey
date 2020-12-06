package listener;

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
 * @since 01.12.2020
 */
public interface IListenProfModules {

    //TODO implement me
    /**
     *
     * @param profModules профмодули (модуль на коллекцию его частей), которые нужно провалидировать
     * @param regElements вложенные рег элементы (имеющие отношение в профмодулям  из первой мапы). т.е. лишних тут не будет
     * @param mainBonds основная связь проф модуль - рег элемент (лишних нет)
     * @param part2PartBonds связь части проф модуль - часть рег элемента (лишних нет)
     */
    boolean onEvent(Map<EppRegistryProfModule, List<EppRegistryElementPart>> profModules,
                    Map<EppRegistryElement, List<EppRegistryElementPart>> regElements,
                    Collection<MainBond> mainBonds,
                    Collection<Part2PartBond> part2PartBonds);
}
