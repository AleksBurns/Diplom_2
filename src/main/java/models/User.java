package models;

public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() {
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        System.out.println("Access token: \n" + accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }
}