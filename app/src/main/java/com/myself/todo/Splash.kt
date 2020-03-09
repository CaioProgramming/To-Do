package com.myself.todo
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.Utils.Utilities.Companion.RC_SIGN_IN
import com.myself.todo.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class Splash : AppCompatActivity() {
    val  splashBinding: ActivitySplashBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.activity_splash,null,false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(splashBinding.root)
        val myanim2 = AnimationUtils.loadAnimation(this, R.anim.popin)
        SignIn()

    }

    private fun SignIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = Arrays.asList<AuthUI.IdpConfig>(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN)
        } else {
            val i = Intent(this, Mylist.javaClass)
            i.putExtra("novo", false)
            i.putExtra("notification", true)
            startActivity(i)
            this.finish()
        }
    }

    private fun startApp() {
        val i = Intent(this, Mylist::class.java)
        startActivity(i)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(R.drawable.ic_box_open_shape).into(splash)
                val handler = Handler()
                handler.postDelayed({startApp()},2000)
            } else {
                if (response != null) {
                    Toast.makeText(this,"Ocorreu um erro ao fazer login, tentando novamente...",LENGTH_LONG).show()
                    SignIn()
                }
            }

        }
    }


}