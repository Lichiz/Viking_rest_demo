/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.mephi.vikingdemo.service;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

/**
 *
 * @author temdo
 */
@Service
public class AnalyticsService {
    private final VikingStorage vikingStorage;

    @Autowired
    public AnalyticsService(VikingStorage vikingStorage) {
        this.vikingStorage = vikingStorage;
    }

    public int countVikingsOverAge(int age){
        List<Viking> vikings = vikingStorage.findAll();
        return (int) vikings.stream()
                .filter(v -> v.age() > age)
                .count();
    }

    public int countVikingsBelowAge(int age){
        List<Viking> vikings = vikingStorage.findAll();
        return (int) vikings.stream()
                .filter(v -> v.age() < age)
                .count();
    }

    public int countVikingsEqualsAge(int age){
        List<Viking> vikings = vikingStorage.findAll();
        return (int) vikings.stream()
                .filter(v -> v.age() == age)
                .count();
    }

    public int countVikingsNotEqualsAge(int age){
        List<Viking> vikings = vikingStorage.findAll();
        return (int) vikings.stream()
                .filter(v -> v.age() != age)
                .count();
    }

    public int countVikingsBeardSyleAndHairColor(BeardStyle beardStyle, HairColor hairColor){
        List<Viking> vikings = vikingStorage.findAll();
        return (int) vikings.stream()
                .filter(v -> (v.beardStyle() == beardStyle && v.hairColor() == hairColor))
                .count();
    }

    public int countVikingsWithAxes(){
        List<Viking> vikings = vikingStorage.findAll();
        return (int) vikings.stream()
                .filter(v -> {
                    int count = (int) v.equipment().stream().filter(e -> e.name().equals("Axe")).count();
                    return count == 1 || count == 2;
                })
                .count();
    }

    public Viking getAnyVikingOver180(){
        List<Viking> vikings = vikingStorage.findAll();
        return vikings.stream()
                .filter(v -> v.heightCm() > 180)
                .findAny()
                .get();
    }

    public List<Viking> getAllVikingsWithLegendaryEquipment(){
        List<Viking> vikings = vikingStorage.findAll();
        return vikings.stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(i -> i.quality().equals("Legendary")))
                .toList();
    }

    public List<Viking> getRedBeardedVikingsSortedByAge(){
        List<Viking> vikings = vikingStorage.findAll();
        return vikings.stream()
                .filter(v -> (v.hairColor().equals(HairColor.Red) && !v.beardStyle().equals(BeardStyle.CLEAN_SHAVEN)))
                .sorted(Comparator.comparing(Viking::age))
                .toList();
    }

    public Integer getMaxId(){
        List<Viking> vikings = vikingStorage.findAll();
        return vikings.stream().max(Comparator.comparing(Viking::id)).get().id();
    }

    public List<Integer> getEvenIds(){
        List<Viking> vikings = vikingStorage.findAll();
        return vikings.stream()
                .map(Viking::id)
                .filter(id -> id % 2 == 0)
                .toList();
    }
}