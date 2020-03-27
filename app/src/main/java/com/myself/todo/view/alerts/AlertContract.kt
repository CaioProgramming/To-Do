package com.myself.todo.view.alerts

import androidx.viewbinding.ViewBinding

interface AlertContract {
    fun setupAlert()
    var theme: Int
    var viewBinding: ViewBinding

    interface AlertListener {
        fun doSomethingBeforeShow()
        fun doSomethingBeforeDismiss()

    }

    interface ActionsListener {
        fun primaryAction()
        fun secondaryAction()
    }
}