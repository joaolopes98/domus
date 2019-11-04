package application.model;

import application.controller.object.Recipe;

import java.util.List;

public abstract class RecipeModel {
    public static boolean create(Recipe recipe){
        return GenericModel.create(recipe);
    }

    public static List<Recipe> getAll(String search){
        return GenericModel.getAll("FROM Recipe " + search);
    }
}
