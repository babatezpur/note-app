package com.babapp.noteapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.babapp.noteapp.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@LauncherActivity , MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}