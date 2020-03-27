package com.myself.todo.view.alerts

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import com.github.mmin18.widget.RealtimeBlurView
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import com.myself.todo.R

abstract class AlertBase(val activity: Activity): DialogInterface.OnShowListener,DialogInterface.OnDismissListener,AlertContract{
    val dialog = Dialog(activity, R.style.Dialog_No_Border)
    val blurView:RealtimeBlurView = activity.findViewById(R.id.rootblur)

    override fun onShow(dialog: DialogInterface?) {
        makesomethingonShow()
        blurView.fadeIn()
    }

    fun makesomethingonShow(){}
    fun makesomethingonDismiss(){}

    override fun onDismiss(dialog: DialogInterface?) {
        blurView.fadeOut()
        makesomethingonDismiss()
    }



}