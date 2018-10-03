package com.myself.todo.Beans;

public class Album {
    private String userID;
    private String fotouri;
    private String description;
    private String dia;
    private String status;
    private String id;


    public Album(String id, String fotouri, String description, String dia, String status, String userID) {
        this.fotouri = fotouri;
        this.description = description;
        this.dia = dia;
        this.status = status;
        this.id = id;
        this.userID = userID;
    }

    public Album() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }



    public String getFotouri() {
        return fotouri;
    }

    public void setFotouri(String fotouri) {
        this.fotouri = fotouri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
