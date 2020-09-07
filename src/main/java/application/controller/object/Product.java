package application.controller.object;

import application.model.Model;

import java.util.Date;


public class Product implements Model {
    private int id;
    private String name;
    private String ean;
    private double cost;
    private double price;
    private int quantity;
    private boolean status;
    private Date shelf_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getShelf_date() {
        return shelf_date;
    }

    public void setShelf_date(Date shelf_date) {
        this.shelf_date = shelf_date;
    }
}
