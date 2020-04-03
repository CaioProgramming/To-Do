package com.myself.todo.view.alerts

import android.app.Activity
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.myself.todo.R
import com.myself.todo.adapters.FotosPopupPager
import com.myself.todo.databinding.PopupPagerBinding
import com.myself.todo.model.FotosDB
import com.myself.todo.model.beans.Album

class FotoAlert(activity: Activity, val fotos: ArrayList<Album>, val position: Int) : AlertBase(activity) {
    override fun setupAlert() {
        val popupPagerBinding: PopupPagerBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.popup_pager, null, false)
        setView(popupPagerBinding.root)
        popupPagerBinding.fotospager.adapter = FotosPopupPager(activity, fotos)
        popupPagerBinding.fotospager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    print("page scrolled")
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    print("page scrolled")
                }

                override fun onPageSelected(position: Int) {
                    val album = fotos[position]
                    popupPagerBinding.dlgfavcheck.isChecked = album.favorite
                }

            })
        popupPagerBinding.dlgexcluir.setOnClickListener {
                val a = fotos[popupPagerBinding.fotospager.currentItem]
                FotosDB(activity).remover(a.id!!)
                dialog.dismiss()
            }
        popupPagerBinding.dlgfavcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                val a = fotos[popupPagerBinding.fotospager.currentItem]
                a.favorite = isChecked
                FotosDB(activity).alterar(a.id!!, a)
            }
        popupPagerBinding.fotospager.setCurrentItem(position, true)
        dialog.show()
    }


}