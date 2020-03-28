package com.myself.todo.view.activities

import MainPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.myself.todo.R
import com.myself.todo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        mainPager.adapter = MainPagerAdapter(supportFragmentManager)
        createtabs()
        setContentView(mainBinding.root)
    }

    private fun createtabs(){
        navigation.setupWithViewPager(mainPager)
        navigation.getTabAt(2)?.icon = getDrawable(R.drawable.box)
        navigation.getTabAt(0)?.icon = getDrawable(R.drawable.objective)
        navigation.getTabAt(1)?.icon = getDrawable(R.drawable.album)
    }



    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

}