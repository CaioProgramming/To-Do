package com.myself.todo.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.myself.todo.R
import com.myself.todo.databinding.ActivityProfileBinding
import com.myself.todo.model.EventsDB
import com.myself.todo.model.FotosDB
import com.myself.todo.view.alerts.AlertContract
import com.myself.todo.view.alerts.ChangeNameAlert
import de.mateware.snacky.Snacky
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.cardlayoutfotos.*

class ActivityProfileEdit : AppCompatActivity(), AlertContract.AlertListener {
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityProfileBinding: ActivityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        activityProfileBinding.user = this.user
        wipeoutbtn.setOnClickListener {
            deleteEvents()
            deletePics()
        }
        fotosbtn.setOnClickListener { deletePics() }
        objetivesbtn.setOnClickListener { deleteEvents() }
        name_layout.setOnClickListener {
            val changeNameAlert = ChangeNameAlert(this)
            changeNameAlert.alertListener = this
        }
        user?.let { setupUser() }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { goBack() }
        supportActionBar?.title = ""
        profile_pic.setOnClickListener { startPicker() }
        val handler = Handler()
        handler.postDelayed({
            viewShimmer.stopShimmer()
            viewShimmer.hideShimmer()
        }, 3000)
        setContentView(activityProfileBinding.root)


    }


    private fun setupUser() {
        Glide.with(this).load(user?.photoUrl).error(R.drawable.camera).into(profile_pic)
        username.text = Html.fromHtml(" Olá <b>${user!!.displayName}</b>")

    }

    private fun deleteEvents() {
        EventsDB(this).remover("")
    }

    private fun deletePics() {
        FotosDB(this).remover("")
    }

    private fun startPicker() {
        TedBottomPicker.with(this)
                .show { uri ->
                    val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
                    user?.updateProfile(profileChangeRequest)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snacky.builder().setActivity(this).success().setText("Foto de perfil alterada").show()
                            setupUser()
                        } else {
                            Snacky.builder().setActivity(this).success().setText("Erro " + task.exception).show()
                        }
                    }
                }

    }

    private fun goBack() {
        val i = Intent(this, Splash::class.java)
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun doSomethingBeforeShow() {
        mainshimmer.showShimmer(true)

    }

    override fun doSomethingBeforeDismiss() {
        viewShimmer.stopShimmer()
        viewShimmer.hideShimmer()
    }


}