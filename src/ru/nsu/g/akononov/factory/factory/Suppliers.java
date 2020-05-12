package ru.nsu.g.akononov.factory.factory;

import ru.nsu.g.akononov.factory.factory.car.Accessory;
import ru.nsu.g.akononov.factory.factory.car.Body;
import ru.nsu.g.akononov.factory.factory.car.Engine;
import ru.nsu.g.akononov.factory.factory.maketing.DetailSupplier;
import ru.nsu.g.akononov.factory.factory.observable.Observer;

import java.util.LinkedList;

public class Suppliers {

    final private DetailSupplier<Body> bodySupplier;
    final private DetailSupplier<Engine> engineSupplier;
    LinkedList<DetailSupplier<Accessory>> accessorySuppliers = new LinkedList<>();
    final private int accessoriesSuppliersCount;

    public Suppliers(Storage storage, int accessoriesSuppliersCount, int bodyDelay, int engineDelay, int accessoryDelay)
    {
        bodySupplier = new DetailSupplier<>(storage.getBodiesStorageCapacity(), storage.getBodiesStorage(), Body::new, bodyDelay);
        engineSupplier = new DetailSupplier<>(storage.getEnginesStorageCapacity(), storage.getEnginesStorage(), Engine::new, engineDelay);

        for (int i = 0; i < accessoriesSuppliersCount; i++) {
            accessorySuppliers.add(new DetailSupplier<>(storage.getAccessoriesStorageCapacity(),
                    storage.getAccessoriesStorage(), Accessory::new, accessoryDelay));
        }

        this.accessoriesSuppliersCount = accessoriesSuppliersCount;

        startWork();
    }

    public void registerObserver(Observer observer)
    {
        bodySupplier.registerObserver(observer);
        engineSupplier.registerObserver(observer);
        for (var accessorySupplier : accessorySuppliers)
        {
            accessorySupplier.registerObserver(observer);
        }
    }

    private void startWork()
    {
        for (int i = 0; i < accessoriesSuppliersCount; i++) {
            accessorySuppliers.get(i).start();
        }

        bodySupplier.start();
        engineSupplier.start();
    }

    private void stopWork()
    {
        for (int i = 0; i < accessoriesSuppliersCount; i++) {
            accessorySuppliers.get(i).stop();
        }

        bodySupplier.stop();
        engineSupplier.stop();
    }
}
