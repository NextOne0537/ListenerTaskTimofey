package listener;

import domain.bonds.MainBond;
import domain.bonds.Part2PartBond;
import domain.parts.EppRegistryElementPart;
import domain.versions.EppRegistryElement;
import domain.versions.EppRegistryProfModule;

import java.util.*;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class Listener implements IListenProfModules {

    public static IListenProfModules INSTANCE = new Listener();

    private Listener() {}

    @Override
    public boolean onEvent(Map<EppRegistryProfModule, List<EppRegistryElementPart>> profModules, Map<EppRegistryElement, List<EppRegistryElementPart>> regElements, Collection<MainBond> mainBonds, Collection<Part2PartBond> part2PartBonds) {

        Map<EppRegistryProfModule, List<MainBond>> profMainBonds = new HashMap<>();
        Map<EppRegistryProfModule, List<Part2PartBond>> profPartBonds = new HashMap<>();
        //временные мапы для удаления элементов в цикле
        Map<EppRegistryProfModule, List<MainBond>> temporal_profMainBonds = new HashMap<>();
        Map<EppRegistryProfModule, List<Part2PartBond>> temporal_profPartBonds = new HashMap<>();

        for (MainBond bond : mainBonds) {
            profMainBonds.putIfAbsent(bond.getModule(), new ArrayList<>());
            profMainBonds.get(bond.getModule()).add(bond);
        }


        for (Part2PartBond bond : part2PartBonds) {
            profPartBonds.putIfAbsent(bond.getMainBond().getModule(), new ArrayList<>());
            profPartBonds.get(bond.getMainBond().getModule()).add(bond);
        }

        for (EppRegistryProfModule profModule : profMainBonds.keySet()) {
            switch (profModule.getState()) {
                case Accepted:
                    for (MainBond mainBond : profMainBonds.get(profModule)) {
                        if (!mainBond.getElement().getState().isAccepted())
                            throw new IllegalStateException("Нельзя согласовать профмодуль " + mainBond.getModule().getTitle() +
                                    ", т.к. вложенные мероприятия имеют неподходящее состояние");

                    }
                    temporal_profMainBonds.put(profModule, profMainBonds.get(profModule));
                    temporal_profPartBonds.put(profModule, profPartBonds.get(profModule));
                    break;
                case Acceptable:
                    for (MainBond mainBond : profMainBonds.get(profModule)) {
                        if (!mainBond.getElement().getState().isAccepted() && !mainBond.getElement().getState().isAcceptable())
                            throw new IllegalStateException
                                    ("Нельзя отправить на согласование профмодуль " + mainBond.getModule().getTitle() +
                                            ", т.к. вложенные мероприятия имеют неподходящее состояние");
                    }
                    temporal_profMainBonds.put(profModule, profMainBonds.get(profModule));
                    temporal_profPartBonds.put(profModule, profPartBonds.get(profModule));
                    break;
                default:
                    break;
            }
        }
        profMainBonds=temporal_profMainBonds;
        profPartBonds=temporal_profPartBonds;

        //Проверка "распределены ли все элементы"

        for (Map.Entry <EppRegistryProfModule, List<MainBond>> pair : profMainBonds.entrySet()){
            EppRegistryProfModule profModule = pair.getKey();
            List<MainBond> mainBondList = pair.getValue();
            List<Part2PartBond> part2PartBondList = profPartBonds.get(profModule);
            for (MainBond bond : mainBondList){
                int partsNumber_Should_Be = regElements.get(bond.getElement()).size();
                int partsNUmber_Actual = 0;
                HashMap<Integer,Integer> orderOfParts = new HashMap<>();

                for (Part2PartBond partBond : part2PartBondList) if (partBond.getMainBond()==bond) {
                    partsNUmber_Actual++;
                    orderOfParts.putIfAbsent(partBond.getModulePart().getNumber(),partBond.getElementPart().getNumber()); // здесь заполняю мапу номерами частей элементов и профомдулей
                }


                if (partsNUmber_Actual!=partsNumber_Should_Be){
                    if (bond.getModule().getState().isAcceptable()) throw new IllegalStateException("" +
                            "Нельзя отправить на согласование профмодуль "+profModule.getTitle()+", т.к. распределены не все части вложенных элементов.");
                    else throw new IllegalStateException("Нельзя согласовать профмодуль "+profModule.getTitle()+", т.к. распределены не все части вложенных элементов.");
                }

                int currentNumberShouldBe = 1;
                if (partsNumber_Should_Be!=0)
                    for (Map.Entry<Integer,Integer> currentNumbers : orderOfParts.entrySet()){
                        if (currentNumbers.getValue()!=currentNumberShouldBe){
                            if (bond.getModule().getState().isAcceptable()) throw new IllegalStateException ("Нельзя отправить на согласование профмодуль "+profModule.getTitle()+", т.к. части вложенного мероприятия "+bond.getElement().getTitle()+" распределены в неправильном порядке.");
                            else throw new IllegalStateException("Нельзя согласовать профмодуль "+profModule.getTitle()+", т.к. части вложенного мероприятия "+bond.getElement().getTitle()+" распределены в неправильном порядке.");
                        }
                        currentNumberShouldBe++;
                    }
            }
        }

        return true;
    }
}