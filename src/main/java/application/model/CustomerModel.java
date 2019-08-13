package application.model;

import application.controller.object.Customer;

import java.util.List;

public abstract class CustomerModel {
    public static List<Customer> getAll(String search) {
        return GenericModel.getAll("FROM Customer " + search);
    }

    public static Customer get(String search){
        return (Customer) GenericModel.get("FROM Customer " + search);
    }

    public static boolean update(Customer customer) {
        return GenericModel.update(customer);
    }

    public static boolean create(Customer customer){
        return GenericModel.create(customer);
    }
}
