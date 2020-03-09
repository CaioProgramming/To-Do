package com.myself.todo.Beans

import org.junit.runner.RunWith

class User {
    private var codigo = 0
    private var user: String? = null
    private var password: String? = null
    private var sexo: String? = null
    fun getSexo(): String? {
        return sexo
    }

    fun setSexo(sexo: String?) {
        this.sexo = sexo
    }

    private var profilepic: String? = null
    fun getProfilepic(): String? {
        return profilepic
    }

    fun setProfilepic(profilepic: String?) {
        this.profilepic = profilepic
    }

    /*public User(Integer id, String usuario, String senha,String email, String telefone) {
        codigo = id;
        user = usuario;
        password = senha;
        this.email = email;
        phone = telefone;
    }*/
    fun getCodigo(): Int {
        return codigo
    }

    fun setCodigo(codigo: Int) {
        this.codigo = codigo
    }

    fun getUser(): String? {
        return user
    }

    fun setUser(user: String?) {
        this.user = user
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }
}