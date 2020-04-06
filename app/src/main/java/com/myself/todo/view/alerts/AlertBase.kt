package com.myself.todo.view.alerts

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.myself.todo.R

abstract class AlertBase(val activity: Activity): DialogInterface.OnShowListener,DialogInterface.OnDismissListener,AlertContract{
    var dialog = Dialog(activity)
    private val blurView: RealtimeBlurView = activity.findViewById(R.id.rootblur)

    fun setTheme(theme: Int?) {
        dialog = if (dialog.javaClass == Dialog::class.java) {
            if (theme != null) Dialog(activity, theme) else Dialog(activity)
        } else {
            if (theme != null) BottomSheetDialog(activity, theme) else BottomSheetDialog(activity)
        }
    }

    fun changeToBottom() {
        dialog = BottomSheetDialog(activity)
    }


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


    fun setView(view: View) {
        dialog.setOnShowListener(this)
        dialog.setOnDismissListener(this)
        dialog.setContentView(view)
        configureview()

    }




    fun dimiss() {
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }
    override fun onShow(dialog: DialogInterface?) {
        alertListener.doSomethingBeforeShow()
        blurView.visibility = VISIBLE
    }


    override fun onDismiss(dialog: DialogInterface?) {
        alertListener.doSomethingBeforeDismiss()
        blurView.visibility = GONE
    }



}