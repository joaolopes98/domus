package application.model;

import application.controller.object.Service;

import java.util.List;

public abstract class ServiceModel {
    public static List<Service> getAllSellable(){
        return (List<Service>) GenericModel.getAll("FROM Service WHERE status = true");
    }

    public static List<Service> getAll(String search) {
        return GenericModel.getAll("FROM Service " + search);
    }

    public static Service get(String search){
        return (Service) GenericModel.get("FROM Service " + search);
    }

    public static boolean update(Service service) {
        return GenericModel.update(service);
    }

    public static boolean create(Service service){
        return GenericModel.create(service);
    }
}
