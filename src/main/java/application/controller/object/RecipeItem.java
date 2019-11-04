package application.controller.object;

import application.controller.MedicineItem;

public class RecipeItem {
    private int id;
    private String name;
    private int quantity;
    private String quantityUnity;
    private int time;
    private int timeMetric;
    private String timeUnity;
    private Recipe recipe;


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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
