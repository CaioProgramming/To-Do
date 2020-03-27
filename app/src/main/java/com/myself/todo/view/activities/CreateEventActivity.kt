package com.myself.todo.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.Beans.Events
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.adapters.CreateEventPagerAdapter
import com.myself.todo.databinding.ActivityCreateEventBinding
import com.myself.todo.model.EventsDB
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_create_event.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateEventActivity : AppCompatActivity(),TextView.OnEditorActionListener {
    val event = Events()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityCreateEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_event)
        event.tasks = ArrayList()
        event.data = actualday()
        viewpager.adapter = CreateEventPagerAdapter(this,cardview,event,this)
        setSupportActionBar(toolbar)
        setContentView(actbind.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_event_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) salvar()
        return super.onOptionsItemSelected(item)
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
            event.UserID = user.uid
            if (event.evento.isNullOrBlank()){
                Snacky.builder().setActivity(this).error().setText("Você precisa dar um nome a esse evento.").show()
                viewpager.setCurrentItem(0,true)
            }
            EventsDB(this).inserir(event)
        }else{
            Snacky.builder().setActivity(this).error().setText("Faça login para adicionar eventos.").setAction("OK") {
                signIn()
            }.show()
        }
    }


    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        salvar()
        return false
    }
}
