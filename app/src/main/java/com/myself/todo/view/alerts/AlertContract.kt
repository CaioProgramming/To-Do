package com.myself.todo.view.alerts

interface AlertContract {
    fun setupAlert()
    fun configureview()
    var theme: Int

    interface AlertListener {
        fun doSomethingBeforeShow()
        fun doSomethingBeforeDismiss()

    }

    interface ActionsListener {
        fun primaryAction()
        fun secondaryAction()
    }
}