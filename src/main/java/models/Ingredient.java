package models;

public class Ingredient {

    private String _id;
    private String name;
    private String type;
    private String proteins;
    private String fat;
    private String carbohydrates;
    private String calories;
    private String price;
    private String image;
    private String image_mobile;
    private String image_large;
    private String __v;

    public Ingredient(String _id, String name, String type, String proteins, String fat, String carbohydrates, String calories, String price, String image, String image_mobile, String image_large, String __v) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.price = price;
        this.image = image;
        this.image_mobile = image_mobile;
        this.image_large = image_large;
        this.__v = __v;
    }

    public Ingredient(){

    }

    public String get_id() {
        return _id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getProteins() {
        return proteins;
    }
    public String getFat() {
        return fat;
    }
    public String getCarbohydrates() {
        return carbohydrates;
    }
    public String getCalories() {
        return calories;
    }
    public String getPrice() {
        return price;
    }
    public String getImage() {
        return image;
    }
    public String getImage_mobile() {
        return image_mobile;
    }
    public String getImage_large() {
        return image_large;
    }
    public String get__v() {
        return __v;
    }

    public Ingredient set_id(String _id) {
        this._id = _id;
        return this;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setProteins(String proteins) {
        this.proteins = proteins;
    }
    public void setFat(String fat) {
        this.fat = fat;
    }
    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
    public void setCalories(String calories) {
        this.calories = calories;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setImage_mobile(String image_mobile) {
        this.image_mobile = image_mobile;
    }
    public void setImage_large(String image_large) {
        this.image_large = image_large;
    }
    public void set__v(String __v) {
        this.__v = __v;
    }
}