package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Recipe implements Model {
    private int id;
    private Access user;
    private Animal animal;
    private Timestamp date;

    private Set<RecipeItem> recipeItems = new HashSet<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Access getUser() {
        return user;
    }

    public void setUser(Access user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Set<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    public void setRecipeItems(Set<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
    }
}
