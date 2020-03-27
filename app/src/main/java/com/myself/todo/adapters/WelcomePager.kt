package com.myself.todo.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mikhaellopez.rxanimation.fadeIn
import com.myself.todo.R
import com.myself.todo.Utils.Utilities
import com.myself.todo.databinding.OnBoardPageBinding
import com.myself.todo.view.activities.MainActivity

class WelcomePager(val activity: Activity) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val onBoardPageBinding: OnBoardPageBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.on_board_page, container, false)
        val onboard = Utilities.onBoardScreens[position]
        onBoardPageBinding.onBoard = onboard
        Glide.with(activity).load(onboard.icon).into(onBoardPageBinding.icon)
        onBoardPageBinding.mainview.fadeIn()
        onBoardPageBinding.startAppbtn.visibility = if (position == Utilities.onBoardScreens.lastIndex) {
            VISIBLE
        } else {
            GONE
        }
        onBoardPageBinding.startAppbtn.setOnClickListener { startMainAct() }
        container.addView(onBoardPageBinding.root)
        return onBoardPageBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as LinearLayout)
    }

    fun startMainAct() {
        val i = Intent(activity, MainActivity::class.java)
        activity.startActivity(i)
    }

    override fun getCount(): Int {
        return Utilities.onBoardScreens.size
    }
}