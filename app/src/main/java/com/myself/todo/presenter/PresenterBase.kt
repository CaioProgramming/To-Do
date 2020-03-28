package com.myself.todo.presenter

import android.app.Activity
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth

abstract class PresenterBase(val activity: Activity, viewBinding: ViewBinding) : PresenterContract {
    val user = FirebaseAuth.getInstance().currentUser


}
