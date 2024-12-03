
package models;

import java.util.List;

public class Order {
    private List<Ingredient> data;

    public List<Ingredient> getData() {
        return this.data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }

    public Order(List<Ingredient> data) {
        this.data = data;
    }

    public Order() {
    }

    public String serialize() {
        if (this.data == null) {
            return "";
        } else {
            StringBuilder resultString = new StringBuilder("{\"ingredients\" :[");

            for(int i = 0; i < this.data.size(); ++i) {
                resultString.append("\"").append(((Ingredient)this.data.get(i)).get_id()).append("\"");
                if (this.data.size() > 1 && i != this.data.size() - 1) {
                    resultString.append(", ");
                }
            }
            resultString.append("]}");
            return resultString.toString();
        }
    }
}