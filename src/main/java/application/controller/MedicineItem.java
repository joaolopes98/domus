package application.controller;

import application.view.auxiliary.Formatter;

public class MedicineItem {
    private String name;
    private int quantity;
    private String quantityUnity;
    private int time;
    private int timeMetric;
    private String timeUnity;


    public MedicineItem(MedicineField medicineField){
        this.name = medicineField.getTxtName().getText();
        this.quantity = Formatter.unmaskInteger(medicineField.getTxtQuantity().getText());
        this.quantityUnity = medicineField.getComboQuantity().getSelectionModel().getSelectedItem();
        this.time = Formatter.unmaskInteger(medicineField.getTxtTime().getText());
        this.timeMetric = Formatter.unmaskInteger(medicineField.getTxtTime2().getText());
        this.timeUnity = medicineField.getComboTime().getSelectionModel().getSelectedItem();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnity() {
        return quantityUnity;
    }

    public void setQuantityUnity(String quantityUnity) {
        this.quantityUnity = quantityUnity;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeMetric() {
        return timeMetric;
    }

    public void setTimeMetric(int timeMetric) {
        this.timeMetric = timeMetric;
    }

    public String getTimeUnity() {
        return timeUnity;
    }

    public void setTimeUnity(String timeUnity) {
        this.timeUnity = timeUnity;
    }
}
