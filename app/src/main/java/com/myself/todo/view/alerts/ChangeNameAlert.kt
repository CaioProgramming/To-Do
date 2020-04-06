package com.myself.todo.view.alerts

import android.app.Activity
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.myself.todo.R
import com.myself.todo.databinding.ChangeNameAlertBinding


class ChangeNameAlert(activity: Activity) : AlertBase(activity) {
    private var changeNameAlertBinding: ChangeNameAlertBinding? = null
    val user = FirebaseAuth.getInstance().currentUser


    override fun setupAlert() {
        changeNameAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.change_name_alert, null, false)
        setTheme(R.style.AppTheme)
        setView(changeNameAlertBinding!!.root)
        changeNameAlertBinding?.let { configureview() }
    }

    override fun configureview() {
        changeNameAlertBinding!!.title.text = Html.fromHtml("Eai <b>${user!!.displayName}, veio dar uma repaginada no nome?")
        changeNameAlertBinding!!.save.isEnabled = false
        changeNameAlertBinding!!.newname.setOnKeyListener { v, keyCode, event ->
            changeNameAlertBinding!!.newname.isEnabled = changeNameAlertBinding!!.newname.text.isNullOrBlank()
            return@setOnKeyListener false
        }
        changeNameAlertBinding!!.newname.setOnEditorActionListener { v, actionId, event ->
            updateUserName(v.text.toString())
            return@setOnEditorActionListener false

        }
        changeNameAlertBinding!!.save.setOnClickListener {
            updateUserName(changeNameAlertBinding!!.newname.text.toString())
        }
        show()
    }

    private fun success() {
        changeNameAlertBinding!!.title.text = "Nome alterado com sucesso!"
        changeNameAlertBinding!!.newname.visibility = GONE
        changeNameAlertBinding!!.save.visibility = GONE
        Glide.with(activity).load(R.drawable.ic_undraw_success).into(changeNameAlertBinding!!.icon)
        val handler = Handler()
        handler.postDelayed({
            dialog.dismiss()
        }, 5500)

    }

    private fun error(message: String) {
        changeNameAlertBinding!!.title.text = message
        changeNameAlertBinding!!.newname.requestFocus()
        changeNameAlertBinding!!.newname.visibility = VISIBLE
        changeNameAlertBinding!!.save.visibility = VISIBLE
        Glide.with(activity).load(R.drawable.ic_undraw_warning).into(changeNameAlertBinding!!.icon)

    }

    private fun updateUserName(name: String) {
        if (!name.isBlank()) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    success()
                } else {
                    error("Ocorreu um erro ao atualizar seu nome \uD83E\uDD14")
                }
            }
        } else {
            error("Não da pra ter um nome vazio, amigo")
        }
    }


}