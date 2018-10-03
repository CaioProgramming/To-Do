package com.myself.todo.Beans;

public class Events {

    private String evento;
    String descricao;
    private String UserID;
    private String data;
    private String id;
    private String status;


    public Events(String id, String evento, String descricao, String usuario, String data, String status) {
        this.evento = evento;
        this.UserID = usuario;
        this.data = data;
        this.id = id;
        this.descricao = descricao;
        this.status = status;
    }

    public Events() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String usuario) {
        this.UserID = usuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {


        this.data = data;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
