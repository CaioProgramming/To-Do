package com.myself.todo.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.format.Time
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.BSImagePicker.OnSingleImageSelectedListener
import com.asksira.bsimagepicker.Utils
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myself.todo.Beans.Album
import com.myself.todo.Database.AlbumRepository
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import de.mateware.snacky.Snacky
import java.io.IOException
import java.util.*

class NewPicActivity : AppCompatActivity(), OnSingleImageSelectedListener {
    var fotopic: ImageView? = null
    var desc: EditText? = null
    var album: Album? = null
    var photouri: Uri? = null
    var albRepository: AlbumRepository? = null
    var usuario: String? = null
    var raiz: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pic)
        raiz = FirebaseDatabase.getInstance().getReference("album")
        album = Album()
        fotopic = findViewById<ImageView?>(R.id.pic)
        desc = findViewById<EditText?>(R.id.picdesc)
        desc?.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN &&
                    keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (!keyEvent.isShiftPressed) {
                    Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!")
                    when (view.id) {
                        1 -> save()
                    }
                    return@OnKeyListener true
                }
            }
            false // pass on to other listeners.
        })
        Picalert()
        fotopic.setOnClickListener(View.OnClickListener { Picalert() })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageselect: Intent?) {
        try {
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageselect.getData()
                try {
                    fotopic.setImageBitmap(Utilities.Companion.handleSamplingAndRotationBitmap(this, selectedImage))
                    album.setFotouri(selectedImage.toString())
                    onBackPressed()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                //fotopic.setImageURI(selectedImage);
//album.setFotouri(Objects.requireNonNull(selectedImage).toString());
                println(album.getFotouri())
                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                    try {
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            1 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = imageselect.getData()
                try {
                    fotopic.setImageBitmap(Utilities.Companion.handleSamplingAndRotationBitmap(this, selectedImage))
                    album.setFotouri(selectedImage.toString())
                    onBackPressed()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                println(album.getFotouri())
                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                }
            }
        }
    }

    private fun succes() {
        Snacky.builder()
                .setActivity(this)
                .setText("Foto adcionada,pressione o botão de voltar para sair!")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show()
        //onBackPressed();
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

    fun salvar(view: View?) {
        if (desc.getText().toString() == "") {
            Snacky.builder()
                    .setActivity(this)
                    .setText("Escreva alguma coisa sobre a foto!")
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
                    .show()
        } else {
            save()
        }
    }

    private fun save() {
        val datenow = Calendar.getInstance().time
        val today = Time(Time.getCurrentTimezone())
        today.setToNow()
        //String time = String.valueOf(today);
        val dia = datenow.toString()
        val user = FirebaseAuth.getInstance().currentUser
        if (!TextUtils.isEmpty(desc.toString())) {
            val id = raiz.push().key
            val album = Album(id, photouri.toString(), desc.getText().toString(), dia, "N", user.getUid())
            raiz.child(id).setValue(album).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    succes("Foto adicionada ")
                } else {
                    error("Erro " + task.exception)
                }
            }
        } else {
            Snacky.builder().setActivity(this).error().setText("Escreva algo sobre a foto").show()
        }
    }

    fun succes(s: String?) {
        Snacky.builder().setActivity(this).success().setText(s).show()
    }

    fun error(s: String?) {
        Snacky.builder().setActivity(this).success().setText(s).show()
    }

    override fun onSingleImageSelected(uri: Uri?) {
        photouri = uri
        Glide.with(this).load(uri).into(fotopic)
        album.setFotouri(uri.toString())
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    companion object {
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    }
}