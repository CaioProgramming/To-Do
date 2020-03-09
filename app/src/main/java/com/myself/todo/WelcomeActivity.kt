package com.myself.todo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.BSImagePicker.OnSingleImageSelectedListener
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith

class WelcomeActivity : AppCompatActivity(), OnSingleImageSelectedListener {
    var mAuth: FirebaseAuth? = null
    var act: RelativeLayout? = null
    var profileform: LinearLayout? = null
    var profilepic: CircleImageView? = null
    var blur: RealtimeBlurView? = null
    var username: EditText? = null
    var messagewelcome: TextView? = null
    var nameform: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        act = findViewById<RelativeLayout?>(R.id.act)
        profileform = findViewById<LinearLayout?>(R.id.profileform)
        nameform = findViewById<LinearLayout?>(R.id.nameform)
        profilepic = findViewById<CircleImageView?>(R.id.profile_pic)
        blur = findViewById<RealtimeBlurView?>(R.id.blur)
        username = findViewById<EditText?>(R.id.username)
        messagewelcome = findViewById<TextView?>(R.id.messagewelcome)
        val user = FirebaseAuth.getInstance().currentUser
        val `in` = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        val out = AnimationUtils.loadAnimation(this, R.anim.slide_out)
        val countDownTimer = object : CountDownTimer(4000, 100) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                messagewelcome.startAnimation(out)
                nameform.startAnimation(slidein)
                nameform.setVisibility(View.VISIBLE)
                messagewelcome.setVisibility(View.INVISIBLE)
            }
        }.start()
    }

    fun Photo(view: View?) {
        if (username.getText() != null) {
            val myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
            blur.setVisibility(View.VISIBLE)
            blur.setBlurRadius(45f)
            profileform.startAnimation(myanim2)
            profileform.setVisibility(View.VISIBLE)
            val countDownTimer = object : CountDownTimer(2000, 100) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    val singleSelectionPicker = BSImagePicker.Builder("com.myself.fileprovider")
                            .hideGalleryTile() //Default: Integer.MAX_VALUE. Don't worry about performance :)
                            .setSpanCount(5) //Default: 3. This is the number of columns
                            .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                            .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                            .setOverSelectTextColor(R.color.black)
                            .setMultiSelectDoneTextColor(R.color.blue_300)
                            .build()
                    singleSelectionPicker.show(supportFragmentManager, "picker")
                }
            }.start()
        } else {
            Message("Escreva o seu nome antes!", "e")
        }
    }

    fun updateprofilepic(path: Uri?) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val user = FirebaseAuth.getInstance().currentUser
        val context: Context = this
        val activity: Activity = this
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(path.toString()))
                .build()
        user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(context).load(path).into(profilepic)
                        profilepic.startAnimation(animation)
                        Snacky.builder().setActivity(activity).success().setText("Foto de perfil alterada").show()
                    }
                }
    }

    fun Message(message: String?, type: String?) {
        if (type == "e") {
            Snacky.builder().setActivity(this).error().setText("Erro $message").show()
        } else {
            Snacky.builder().setActivity(this).error().setText(message).show()
        }
    }

    fun save(view: View?) {
        val out = AnimationUtils.loadAnimation(this, R.anim.pop_out)
        val i = Intent(this, Mylist::class.java)
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString())
                .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                act.startAnimation(out)
                Message("Cadastro concluído com sucesso " + user.displayName, "s")
                val countDownTimer = object : CountDownTimer(3000, 100) {
                    override fun onTick(l: Long) {}
                    override fun onFinish() {
                        startActivity(i)
                    }
                }.start()
            }
        }
    }

    override fun onSingleImageSelected(uri: Uri?) {
        updateprofilepic(uri)
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
}