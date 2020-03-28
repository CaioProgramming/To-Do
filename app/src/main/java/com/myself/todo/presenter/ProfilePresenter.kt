package com.myself.todo.presenter

import android.app.Activity
import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.google.firebase.auth.UserProfileChangeRequest
import com.myself.todo.adapters.ProfileRecyclerAdapter
import com.myself.todo.databinding.FragmentProfileBinding
import com.myself.todo.view.activities.ActivityProfileEdit
import de.mateware.snacky.Snacky
import gun0912.tedbottompicker.TedBottomPicker

class ProfilePresenter(activity: Activity, val profileBinding: FragmentProfileBinding) : PresenterBase(activity, profileBinding) {
    override fun initview() {
        setupuserinfo()
        loadItems()
    }

    private fun setupuserinfo(){
        profileBinding.username.text = user?.displayName
        Glide.with(activity).load(user?.photoUrl).into(profileBinding.profilepic)
        profileBinding.profileedit.visibility = if (user != null) VISIBLE else GONE
        profileBinding.profilepic.setOnClickListener { startPicker() }
        profileBinding.profileedit.setOnClickListener {
            val intent = Intent(activity,ActivityProfileEdit::class.java)
            activity.startActivity(intent)
        }

    }
    private fun loadItems(){
        profileBinding.useritemsrecycler.adapter = ProfileRecyclerAdapter(activity)
        profileBinding.useritemsrecycler.layoutManager = LinearLayoutManager(activity,VERTICAL,false)
    }
    private fun startPicker() {
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

    }

}