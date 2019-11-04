package application.controller;

import application.controller.object.SaleItem;
import application.view.auxiliary.Formatter;

public class AnimalItemField {

    private String user;
    private String name;
    private String date;

    public AnimalItemField(SaleItem saleItem){
        this.name = saleItem.getService().getName();
        this.date = Formatter.formatDate(saleItem.getSale().getDate());

        if(saleItem.getSale().getSchedule() != null){
            this.user = saleItem.getSale().getSchedule().getAccess().getName();
        } else {
            this.user = saleItem.getSale().getAccess().getName();
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
