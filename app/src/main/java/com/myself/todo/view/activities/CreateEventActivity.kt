package com.myself.todo.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.eftimoff.viewpagertransformers.StackTransformer
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.adapters.CreateEventPagerAdapter
import com.myself.todo.databinding.ActivityCreateEventBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.beans.Events
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_create_event.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateEventActivity : AppCompatActivity(),TextView.OnEditorActionListener {
    private val event = Events()
    var saveItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityCreateEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_event)
        event.tasks = ArrayList()
        event.data = actualday()
        viewpager.adapter = CreateEventPagerAdapter(this, event, this)
        viewpager.setPageTransformer(true, StackTransformer())
        setSupportActionBar(toolbar)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val title = if (position != (viewpager.adapter as CreateEventPagerAdapter).count - 1) "Continuar" else "Salvar"
                saveItem?.title = title
            }

        })
        setContentView(actbind.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_event_menu,menu)
        saveItem = menu?.findItem(R.id.save)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            if (viewpager.currentItem == (viewpager.adapter as CreateEventPagerAdapter).count - 1) {
                salvar()
            } else {
                viewpager.currentItem = viewpager.currentItem + 1
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = listOf<AuthUI.IdpConfig>(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), Utilities.RC_SIGN_IN)
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
            event.userID = user.uid
            if (event.evento.isNullOrBlank()){
                Snacky.builder().setActivity(this).error().setText("Você precisa dar um nome a esse evento.").show()
                viewpager.setCurrentItem(0,true)
                return
            }
            if (event.tasks.size == 0) {
                Snacky.builder().setActivity(this).warning().setText("Tem certeza que deseja salvar sem nenhuma tarefa?").setAction("Sim") {
                    EventsDB(this).inserir(event)
                }.show()
                return
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
