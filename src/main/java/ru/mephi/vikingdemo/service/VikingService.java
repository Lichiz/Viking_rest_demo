package ru.mephi.vikingdemo.service;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mephi.vikingdemo.repository.VikingStorage;

@Service
public class VikingService {
    // каждый раз при изменении создаётся новая копия списка

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;


    @Autowired
    public VikingService(
            VikingFactory vikingFactory,
            VikingStorage vikingStorage
    ) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public List<Viking> createRandomVikings(int lenght){
        List<Viking> vikings = vikingFactory.createRandomVikings(lenght);
        vikings.forEach(v -> vikingStorage.save(v));
        return vikings;
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }
    public void deleteById(int id) {
        vikingStorage.deleteById(id);
    }

    public Viking createCustomViking(Viking viking){
        Viking vikingCreated = vikingFactory.createCustomViking(viking);
        return vikingStorage.save(vikingCreated);

    }
    public boolean updateViking(Viking viking){
        return vikingStorage.updateViking(viking);
    }
}