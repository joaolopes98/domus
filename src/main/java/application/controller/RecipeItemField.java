package application.controller;

public class RecipeItemField {
    private String user;
    private String date;
    private String description;

    public RecipeItemField(String user, String date, String description) {
        this.user = user;
        this.date = date;
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
