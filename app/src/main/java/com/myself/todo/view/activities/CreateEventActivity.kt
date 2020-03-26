package com.myself.todo.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.Beans.Events
import com.myself.todo.Beans.Tarefas
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.databinding.ActivityCreateEventBinding
import com.myself.todo.model.EventsDB
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_create_event.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateEventActivity : AppCompatActivity() {
    val tasklist = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityCreateEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(actbind.root)



    }

    private fun setup(){
        taskEditText.setOnEditorActionListener { v, actionId, event ->
            addChipToGroup(taskEditText.text.toString())
            return@setOnEditorActionListener false
        }
    }
    private fun signIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = Arrays.asList<AuthUI.IdpConfig>(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), Utilities.RC_SIGN_IN)
        } else {
            val i = Intent(this, MainActivity.javaClass)
            i.putExtra("novo", false)
            i.putExtra("notification", true)
            startActivity(i)
            this.finish()
        }
    }

    private fun addChipToGroup(nome: String) {
        val chip = Chip(this)
        chip.text = nome
        chip.isCloseIconVisible = true
        chipGroup.addView(chip)
        tasklist.add(nome)
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
            tasklist.remove(nome)
        }
    }
    private fun actualday(): String {
        val datenow = Calendar.getInstance().time
        @SuppressLint("SimpleDateFormat") val df = SimpleDateFormat("dd/MM/yyyy")
        val dia = df.format(datenow)
        println(dia)
        return dia
    }
    private fun salvar(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val eventname = eventTitle.text.toString()
            val eventdescription = eventDescription.text.toString()
            val event = Events(eventname,eventdescription,user.uid,actualday(),"",false,getTarefas())
            EventsDB(this).inserir(event)
        }else{
            Snacky.builder().setActivity(this).error().setText("Faça login para adicionar eventos.").setAction("OK") {
                signIn()
            }
        }
    }

    private fun getTarefas():ArrayList<Tarefas>{
       val tasks = ArrayList<Tarefas>()
        for (task in tasklist){
            tasks.add(Tarefas(task,actualday(),false))
        }
        return tasks

    }
}
