package com.myself.todo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.BSImagePicker.OnSingleImageSelectedListener
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.myself.todo.Beans.User
import com.myself.todo.Database.DadosOpenHelper
import com.myself.todo.Database.UserRepository
import com.myself.todo.Fragments.EventsFragment
import com.myself.todo.Fragments.FotosFragment
import com.myself.todo.Fragments.MusicFragment
import com.myself.todo.Fragments.ProfileFragment
import de.hdodenhof.circleimageview.CircleImageView
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_mylist.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.junit.runner.RunWith

class Mylist : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylist)
        setSupportActionBar(my_toolbar)
        navigation.setOnTabSelectListener()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Glide.with(this).load(user.photoUrl).into(userpic)
        }

    }

    private fun SetProfilePic(user: TextView?, pic: ImageView?) {
        val intent = intent
        usuario = intent.extras.getString("usuario")
        cod_usuario = intent.extras.getInt("codigo")
        senha = intent.extras.getString("senha")
        usuarioRepositorio = UserRepository(conexao)
        usuarioB = usuarioRepositorio.findByLogin(usuario, senha)
        println(usuarioB.getProfilepic())
        println(usuarioB.getUser())
        println(usuarioB.getSexo())
        println(usuario)
        user.setText(usuario)
        if (usuarioB.getProfilepic() == null) {
        } else {
            Glide.with(this).load(usuarioB.getProfilepic().toString()).into(pic)
        }
    }

    fun foto(view: View?) {
        val singleSelectionPicker = BSImagePicker.Builder("com.myself.fileprovider")
                .hideGalleryTile() //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                .setOverSelectTextColor(R.color.black)
                .setMultiSelectDoneTextColor(R.color.blue_300)
                .build()
        singleSelectionPicker.show(supportFragmentManager, "picker")
    }

    fun newfoto(view: View?) {
        val i = Intent(this, NewPicActivity::class.java)
        i.putExtra("usuario", usuario)
        startActivity(i)
    }

    private fun succes(usuarioB: User?) {
        Snacky.builder()
                .setActivity(this)
                .setText("Foto alterada " + usuarioB.getUser())
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show()
    }

    fun checkPermissionREAD_EXTERNAL_STORAGE(
            context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity?,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Armazenamento externo", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context as Activity?, arrayOf<String?>(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun showDialog(msg: String?, context: Context?,
                   permission: String?) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permissão")
        alertBuilder.setMessage("$msg permissão necessária")
        alertBuilder.setPositiveButton(android.R.string.yes
        ) { dialog, which ->
            ActivityCompat.requestPermissions(context as Activity?, arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    fun updateprofilepic(path: Uri?) {
        val user = FirebaseAuth.getInstance().currentUser
        val context: Context = this
        val activity: Activity = this
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(path.toString()))
                .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context).load(path).into(profilepic)
                Snacky.builder().setActivity(activity).success().setText("Foto de perfil alterada").show()
            }
        }
    }

    override fun onSingleImageSelected(uri: Uri?) {
        updateprofilepic(uri)
        /*/usuarioB.setProfilepic(uri.toString());
        System.out.println(usuarioB.getProfilepic());
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            try {
                criarConexao();
                usuarioRepositorio = new UserRepository(conexao);
                usuarioRepositorio.update(usuarioB);

                succes(usuarioB);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    companion object {
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 112
    }
}