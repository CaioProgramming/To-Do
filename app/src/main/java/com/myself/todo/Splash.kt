package com.myself.todo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.junit.runner.RunWith

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val myanim2 = AnimationUtils.loadAnimation(this, R.anim.popin)
        val iv = findViewById<ImageView?>(R.id.splash)
        iv.startAnimation(myanim2)
        iv.setAnimation(myanim2)
        checkPermissionREAD_EXTERNAL_STORAGE(this)
    }

    private fun StartApp() {
        val i = Intent(this, Login::class.java)
        startActivity(i)
        finish()
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
                                    Mylist.Companion.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                false
            } else {
                StartApp()
                true
            }
        } else {
            StartApp()
            true
        }
    }

    fun checkPermissionWRITE_EXTERNAL_STORAGE(
            context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity?,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("Armazenamento externo", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context as Activity?, arrayOf<String?>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    Mylist.Companion.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                }
                false
            } else {
                StartApp()
                true
            }
        } else {
            StartApp()
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
                    Mylist.Companion.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        val alert = alertBuilder.create()
        alert.show()
    }
}