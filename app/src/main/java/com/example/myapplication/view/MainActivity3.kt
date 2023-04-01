package com.example.myapplication.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class MainActivity3 : AppCompatActivity() {

    private lateinit var data: String
    private lateinit var ip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        data = intent.extras?.get("data").toString()
        ip = intent.extras?.get("ip").toString()
    }

    override fun onResume() {
        super.onResume()

        val dataTv: TextView = findViewById(R.id.textView2)
        val ipTv: TextView = findViewById(R.id.textView4)

        dataTv.text = data
        ipTv.text = ip
    }
}