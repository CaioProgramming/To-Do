package com.myself.todo.view.alerts

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import com.github.mmin18.widget.RealtimeBlurView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.myself.todo.R
import com.myself.todo.Utils.Utilities.Companion.fadeIn
import com.myself.todo.Utils.Utilities.Companion.fadeOut

abstract class BottomAlertBase(val activity: Activity) : DialogInterface.OnShowListener, DialogInterface.OnDismissListener, AlertContract {
    override var theme: Int = R.style.Dialog_No_Border
    val dialog = BottomSheetDialog(activity, theme)
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


    fun setView(view: View) {
        dialog.setOnShowListener(this)
        dialog.setOnDismissListener(this)
        dialog.setContentView(view)

    }


    fun dimiss() {
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }


    override fun onShow(dialog: DialogInterface?) {
        alertListener.doSomethingBeforeShow()
        fadeIn(blurView)
    }


    override fun onDismiss(dialog: DialogInterface?) {
        alertListener.doSomethingBeforeDismiss()
        fadeOut(blurView)
    }


}