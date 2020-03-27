package com.myself.todo.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.myself.todo.R
import com.myself.todo.adapters.WelcomePager
import com.myself.todo.databinding.ActivityWelcomeBinding
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val welcomeBinding: ActivityWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        welcomePager.adapter = WelcomePager(this)
        startSlide()
        setContentView(welcomeBinding.root)
    }

    fun startSlide() {
        val timerTask = object : TimerTask() {
            override fun run() {
                welcomePager.currentItem = welcomePager.currentItem + 1
            }
        }


        val timer = Timer()
        timer.schedule(timerTask, 3000, 3000)
    }

}