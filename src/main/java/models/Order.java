package models;

import java.util.List;

public class Order {
    private List<String> ingredients;
    private String name;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Order(List<String> ingredients, String name, Order order) {
        this.ingredients = ingredients;
        this.name = name;
    }
}