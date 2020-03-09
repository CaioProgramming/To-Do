package com.myself.todo

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import com.dx.dxloadingbutton.lib.LoadingButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import org.junit.runner.RunWith

class Profile : AppCompatActivity() {
    var raiz: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val user = FirebaseAuth.getInstance().currentUser
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val emailedit = findViewById<AutoCompleteTextView?>(R.id.emailedit)
        val wipeoutbtn = findViewById<LoadingButton?>(R.id.wipeoutbtn)
        val savebtn = findViewById<LoadingButton?>(R.id.savebtn)
        val fotosbtn = findViewById<LoadingButton?>(R.id.fotosbtn)
        val objetivesbtn = findViewById<LoadingButton?>(R.id.objetivesbtn)
        val options = findViewById<LinearLayout?>(R.id.options)
        val progress = findViewById<ProgressBar?>(R.id.progress)
        val form = findViewById<LinearLayout?>(R.id.form)
        val usersenha = findViewById<AutoCompleteTextView?>(R.id.usersenha)
        val usernameedit = findViewById<AutoCompleteTextView?>(R.id.usernameedit)
        val top = findViewById<RelativeLayout?>(R.id.top)
        val username = findViewById<TextView?>(R.id.username)
        val userid = findViewById<TextView?>(R.id.userid)
        val profilepic = findViewById<CircleImageView?>(R.id.profile_pic)
        profilepic.setOnClickListener(View.OnClickListener { Picalert() })
        val picback = findViewById<ImageView?>(R.id.picback)
        if (user != null) {
            Glide.with(this).load(user.photoUrl).into(picback)
            Glide.with(this).load(user.photoUrl).into(profilepic)
            username.setText(user.displayName)
            usernameedit.setText(user.displayName)
            userid.setText(user.uid)
        }
        val myanim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val myanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        val myanim3 = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        objetivesbtn.setOnClickListener(View.OnClickListener {
            objetivesbtn.startLoading()
            val eventsreference = FirebaseDatabase.getInstance().getReference("events")
            if (user != null) {
                eventsreference.child("userID").child(user.uid).removeValue().addOnCompleteListener { }
            }
            val countDownTimer = object : CountDownTimer(3000, 100) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    objetivesbtn.reset()
                    Message("Você apagou todos seus objetivos")
                }
            }.start()
        })
        fotosbtn.setOnClickListener(View.OnClickListener {
            fotosbtn.startLoading()
            raiz = FirebaseDatabase.getInstance().getReference("album")
            raiz.keepSynced(true)
            if (user != null) {
                raiz.child("userID").child(user.uid).removeValue().addOnCompleteListener {
                    Message("Você removeu todas as fotos")
                    fotosbtn.loadingSuccessful()
                }
            }
            //objRepository.apagarAll(user.getUser());
            val countDownTimer = object : CountDownTimer(3000, 100) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    fotosbtn.reset()
                }
            }.start()
        })
        savebtn.setOnClickListener(View.OnClickListener {
            savebtn.startLoading()
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameedit.getText().toString())
                    .build()
            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Message("Nome alterado")
                }
            }
            user?.updateEmail(emailedit.getText().toString())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Message("Email alterado")
                }
            }
            user?.updatePassword(usersenha.getText().toString())?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Message("Senha alterada")
                }
            }
            val countDownTimer = object : CountDownTimer(3000, 100) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    savebtn.loadingSuccessful()
                }
            }.start()
        })
        wipeoutbtn.setOnClickListener(View.OnClickListener {
            wipeoutbtn.startLoading()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                raiz = FirebaseDatabase.getInstance().getReference("album")
                raiz.child("userID").child(user.uid).removeValue()
                raiz = FirebaseDatabase.getInstance().getReference("events")
                raiz.child("userID").child(user.uid).removeValue()
                user.delete()
            }
            //objRepository.apagarAll(user.getUser());
            val countDownTimer = object : CountDownTimer(3000, 100) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    wipeoutbtn.loadingSuccessful()
                    Message("Você apagou todos os seus dados")
                    top.startAnimation(myanim3)
                    form.startAnimation(myanim3)
                    options.startAnimation(myanim3)
                    val countDownTimer1 = object : CountDownTimer(2000, 100) {
                        override fun onTick(l: Long) {}
                        override fun onFinish() {
                            top.setVisibility(View.INVISIBLE)
                            form.setVisibility(View.INVISIBLE)
                            options.setVisibility(View.INVISIBLE)
                            GoBack()
                        }
                    }.start()
                }
            }.start()
        })
        val countDownTimer = object : CountDownTimer(3000, 100) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                progress.startAnimation(myanim3)
                progress.setVisibility(View.INVISIBLE)
                top.setVisibility(View.VISIBLE)
                top.startAnimation(myanim2)
                options.setVisibility(View.VISIBLE)
                options.startAnimation(myanim2)
                form.setVisibility(View.VISIBLE)
                form.startAnimation(myanim)
            }
        }.start()
    }

    private fun Picalert() {
        val singleSelectionPicker = BSImagePicker.Builder("com.myself.fileprovider") //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .hideGalleryTile()
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.blue_300)
                .build()
        singleSelectionPicker.show(supportFragmentManager, "picker")
    }

    private fun GoBack() {
        val i = Intent(this, Splash::class.java)
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        val i = Intent(this, Mylist::class.java)
        startActivity(i)
        finish()
    }

    private fun Message(msg: String?) {
        Snacky.builder().setActivity(this).success().setText(msg!!).show()
    }
}