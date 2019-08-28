package application.model;

import application.controller.object.Animal;
import java.util.List;

public class AnimalModel {

    public static List<Animal> getAll(String search) {
        return GenericModel.getAll("FROM Animal " + search);
    }

    public static boolean update(Animal animal){
        return GenericModel.update(animal);
    }

}
