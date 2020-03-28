package com.myself.todo.view.alerts

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.widget.Toast
import com.github.mmin18.widget.RealtimeBlurView
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import com.myself.todo.R

abstract class AlertBase(val activity: Activity): DialogInterface.OnShowListener,DialogInterface.OnDismissListener,AlertContract{
    override var theme: Int = R.style.Dialog_No_Border
    val dialog = Dialog(activity, this.theme)
    private val blurView: RealtimeBlurView = activity.findViewById(R.id.rootblur)

    var alertListener: AlertContract.AlertListener = object : AlertContract.AlertListener {
        override fun doSomethingBeforeShow() {
            Toast.makeText(activity, "Alert is coming...", Toast.LENGTH_LONG).show()
        }

        override fun doSomethingBeforeDismiss() {
            Toast.makeText(activity, "Its time to alert go away...", Toast.LENGTH_LONG).show()
        }
    }

    var actionsListener: AlertContract.ActionsListener = object : AlertContract.ActionsListener {
        override fun primaryAction() {
            Toast.makeText(activity, "Executed primary action", Toast.LENGTH_LONG).show()
        }

        override fun secondaryAction() {
            Toast.makeText(activity, "Executed secondary action", Toast.LENGTH_LONG).show()
            dimiss()
        }
    }


    init {
        this.setupAlert()
        dialog.show()
    }

    fun dimiss() {
        dialog.dismiss()
    }


    override fun onShow(dialog: DialogInterface?) {
        alertListener.doSomethingBeforeShow()
        blurView.fadeIn()
    }


    override fun onDismiss(dialog: DialogInterface?) {
        alertListener.doSomethingBeforeDismiss()
        blurView.fadeOut()
    }



}