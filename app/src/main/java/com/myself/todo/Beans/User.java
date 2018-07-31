package com.myself.todo.Beans;

public class User {

    private int codigo;
    private String user;
    private String password;


    /*public User(Integer id, String usuario, String senha,String email, String telefone) {
        codigo = id;
        user = usuario;
        password = senha;
        this.email = email;
        phone = telefone;
    }*/

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
