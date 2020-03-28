package com.myself.todo.view.alerts

import android.app.Activity
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.myself.todo.R
import com.myself.todo.databinding.MessageAlertBinding

class MessageAlert(activity: Activity, val message: String) : AlertBase(activity) {

    private val messageAlertBinding: MessageAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.message_alert, null, false)
    override fun setupAlert() {
        dialog.setContentView(messageAlertBinding.root)

        messageAlertBinding.primarybtn.setOnClickListener { actionsListener.primaryAction() }
        messageAlertBinding.secondarybtn.setOnClickListener { actionsListener.secondaryAction() }
        messageAlertBinding.message.text = message
    }


}