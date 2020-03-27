package com.myself.todo.view.activities

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.myself.todo.Beans.Album
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.databinding.ActivityNewPicBinding
import com.myself.todo.model.FotosDB
import de.mateware.snacky.Snacky
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_new_pic.*
import java.util.*

class NewPicActivity : AppCompatActivity(),PermissionListener {
    var url: String? = null
    val user = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actbind: ActivityNewPicBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_pic)
        setupview()
        requestPermissions()
        startPicker()
        setContentView(actbind.root)
    }

    private fun setupview(){
        save.setOnClickListener { save() }
        picturecard.setOnClickListener { startPicker() }
        diapic.text = Utilities.actualday()

    }

    private fun startPicker() {
        TedBottomPicker.with(this)
                .show { uri -> url = uri!!.path
                    loadpic()
                }

    }

    private fun requestPermissions(){
        TedPermission.with(this)
                .setPermissionListener(this)
                .setDeniedMessage("Se você não aceitar essa permissão não poderá adicionar fotos...\n\nPor favor ligue as permissões em [Configurações] > [Permissões]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
    }


    private fun loadpic(){
        Glide.with(this).load(url).into(albpic)
    }



    private fun createAlbum():Album{
        return Album(null,url,descricaopic.text.toString(),Utilities.actualday(),false,user?.uid)
    }
    private fun SignIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val providers = Arrays.asList(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme)
                    .build(), Utilities.RC_SIGN_IN)
        }
    }


    private fun save() {
        if (user == null){
            Snacky.builder().error().setText("Você precisa estar logado para salvar.").setAction("Login") {
                SignIn()
            }.show()
            return
        }
        val album = createAlbum()
        if (album.description.isNullOrBlank()){
            Snacky.builder().setActivity(this).warning()
                    .setText("Você está prestes a salvar uma foto sem legenda, deseja salvar assim mesmo?")
                    .setAction("Salvar") {
                        FotosDB(this).inserir(album)
                    }.show()
        }
        FotosDB(this).inserir(createAlbum())
    }







    override fun onPermissionGranted() {
        startPicker()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Snacky.builder().setActivity(this).error().setText("Se não permitir o acesso não da para salvar as fotos...")
    }
}