package com.myself.todo.view.activities
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.myself.todo.R
import com.myself.todo.Utils.Utilities.Companion.RC_SIGN_IN
import com.myself.todo.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashBinding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setContentView(splashBinding.root)
        signIn()

    }

    private fun signIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), RC_SIGN_IN)
        } else {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("novo", false)
            i.putExtra("notification", true)
            startActivity(i)
            this.finish()
        }
    }

    private fun startApp() {
        val i = Intent(this, MainActivity::class.java)
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
                handler.postDelayed({ startApp() }, 2000)
            } else {
                if (response != null) {
                    Toast.makeText(this,"Ocorreu um erro ao fazer login, tentando novamente...",LENGTH_LONG).show()
                    signIn()
                }
            }

        }
    }


}