package com.myself.todo.view.activities

import MainPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.google.android.material.tabs.TabLayoutMediator
import com.myself.todo.R
import com.myself.todo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val fontRequest = androidx.core.provider.FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs)
        val config = FontRequestEmojiCompatConfig(this, fontRequest)
        EmojiCompat.init(config)
        mainPager.adapter = MainPagerAdapter(this)
        createtabs()
        setContentView(mainBinding.root)
    }

    private fun createtabs(){
        TabLayoutMediator(navigation, mainPager) { tab, position ->
            when (position) {
                0 -> tab.icon = getDrawable(R.drawable.objective)
                1 -> tab.icon = getDrawable(R.drawable.album)
                2 -> tab.icon = getDrawable(R.drawable.box)
            }
        }.attach()

    }



    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

}