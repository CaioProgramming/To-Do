package com.myself.todo.presenter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.google.firebase.auth.UserProfileChangeRequest
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.myself.todo.adapters.ProfileRecyclerAdapter
import com.myself.todo.databinding.FragmentProfileBinding
import com.myself.todo.view.activities.ActivityProfileEdit
import de.mateware.snacky.Snacky
import gun0912.tedbottompicker.TedBottomPicker

class ProfilePresenter(activity: Activity, val profileBinding: FragmentProfileBinding) : PresenterBase(activity, profileBinding), PermissionListener {
    override fun initview() {
        setupuserinfo()
        loadItems()
    }

    private fun setupuserinfo(){
        profileBinding.username.text = user?.displayName
        Glide.with(activity).load(user?.photoUrl).into(profileBinding.profilepic)
        profileBinding.profileedit.visibility = if (user != null) VISIBLE else GONE
        profileBinding.profilepic.setOnClickListener {

            startPicker()
        }
        profileBinding.profileedit.setOnClickListener {
            val intent = Intent(activity,ActivityProfileEdit::class.java)
            activity.startActivity(intent)
        }

    }

    private fun requestPermissions() {
        TedPermission.with(activity)
                .setPermissionListener(this)
                .setDeniedMessage("Se você não aceitar essa permissão não poderá adicionar fotos...\n\nPor favor ligue as permissões em [Configurações] > [Permissões]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }

    private fun loadItems(){
        profileBinding.useritemsrecycler.adapter = ProfileRecyclerAdapter(activity)
        profileBinding.useritemsrecycler.layoutManager = LinearLayoutManager(activity,VERTICAL,false)
    }
    private fun startPicker() {
        val permitted = allpermmitted()
        if (permitted) {
            TedBottomPicker.with(activity as FragmentActivity?)
                    .show { uri ->
                        val profileChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
                        user?.updateProfile(profileChangeRequest)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Snacky.builder().setActivity(activity).success().setText("Foto de perfil alterada").show()
                                setupuserinfo()
                            } else {
                                Snacky.builder().setActivity(activity).success().setText("Erro " + task.exception).show()
                            }
                        }
                    }
        } else {
            requestPermissions()
        }

    }

    private fun allpermmitted(): Boolean {
        val write = TedPermission.isGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = TedPermission.isGranted(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        val camera = TedPermission.isGranted(activity, Manifest.permission.CAMERA)
        return write && read && camera
    }

    override fun onPermissionGranted() {
        startPicker()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Snacky.builder().setActivity(activity).error().setText("Se não permitir o acesso não da para salvar as fotos...").show()
    }

}