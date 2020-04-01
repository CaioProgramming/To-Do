package com.myself.todo.adapters

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.databinding.PopupfotocardBinding
import com.myself.todo.model.beans.Album

class FotosPopupPager(val activity: Activity, val fotos: ArrayList<Album>?) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val popupfotocardBinding: PopupfotocardBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.popupfotocard, container, false)
        popupfotocardBinding.album = fotos!![position]
        val alb = fotos[position]
        popupfotocardBinding.diapic.text = Utilities.convertDate(alb.dia)
        popupfotocardBinding.descricaopic.setOnClickListener { enableEdit(popupfotocardBinding.descricaopic) }
        popupfotocardBinding.descricaopic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                print("after text")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                print("before text change")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                popupfotocardBinding.save.visibility = if (!s.isNullOrBlank()) {
                    VISIBLE
                } else {
                    GONE
                }
            }
        })
        container.addView(popupfotocardBinding.root)
        return popupfotocardBinding.root
    }

    private fun enableEdit(tv: TextView) {
        tv.isFocusable = true
        tv.isEnabled = true
        tv.isClickable = true
        tv.isFocusableInTouchMode = true
        tv.requestFocus()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as LinearLayout)
    }

    override fun getCount(): Int {
        return fotos!!.size
    }
}