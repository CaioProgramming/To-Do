package com.myself.todo.view.activities

import MainPagerAdapter
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.myself.todo.R
import com.myself.todo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setSupportActionBar(toolbar)
        mainPager.adapter = MainPagerAdapter(supportFragmentManager)
        createtabs()
        setContentView(mainBinding.root)
    }

    private fun createtabs(){
        navigation.setupWithViewPager(mainPager)
        navigation.getTabAt(3)?.text = Html.fromHtml("<b>Yo</b>u")
        navigation.getTabAt(0)?.icon = getDrawable(R.drawable.objective)
        navigation.getTabAt(1)?.icon = getDrawable(R.drawable.album)
    }



    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

}