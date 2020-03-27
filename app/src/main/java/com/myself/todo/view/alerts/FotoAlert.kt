package com.myself.todo.view.alerts

import android.app.Activity
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.myself.todo.Beans.Album
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.databinding.PopupfotoBinding
import com.myself.todo.model.FotosDB

class FotoAlert(activity: Activity,val album: Album) :AlertBase(activity) {
    private val popupfotoBinding: PopupfotoBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.popupfoto,null,false)
    init {
        dialog.setContentView(popupfotoBinding.root)
        setupAlert()
        dialog.show()
    }

    override fun setupAlert() {
        popupfotoBinding.descricaopic.text = album.description
        popupfotoBinding.dlgfavcheck.isChecked = album.favorite
        popupfotoBinding.diapic.text = Utilities.convertDate(album.dia)
        Glide.with(activity).load(album.fotouri).into(popupfotoBinding.albpic)
        popupfotoBinding.dlgexcluir.setOnClickListener { FotosDB(activity).remover(album.id!!) }
        popupfotoBinding.dlgsalvar.setOnClickListener {
            album.description = popupfotoBinding.descricaopic.text.toString()
            FotosDB(activity).alterar(album.id!!,album)
        }
        popupfotoBinding.dlgfavcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                    album.favorite = isChecked
                    FotosDB(activity).alterar(album.id!!,album)
            }
        popupfotoBinding.descricaopic.setOnClickListener {
                enableEdit(popupfotoBinding.descricaopic)
                popupfotoBinding.dlgsalvar.visibility = VISIBLE
            }
    }

    private fun enableEdit(tv:TextView){
        tv.isFocusable = true
        tv.isEnabled = true
        tv.isClickable = true
        tv.isFocusableInTouchMode = true
        tv.requestFocus()
    }

}