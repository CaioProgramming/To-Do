package com.myself.todo.presenter

import androidx.fragment.app.Fragment

abstract class PresenterBase(val fragment: Fragment): PresenterContract {
    val activity = fragment.activity!!
    init {
        initview()
    }

}