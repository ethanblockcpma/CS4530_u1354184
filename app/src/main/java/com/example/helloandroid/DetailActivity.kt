package com.example.helloandroid // Replace with your actual package name

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.helloandroid.R // Replace with your actual package name if different


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets -> // Use android.R.id.content if no root ID in activity_detail.xml
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textViewDetail: TextView = findViewById(R.id.textViewDetail)

        // Get the data passed from MainActivity
        val buttonInfo = intent.getStringExtra(MainActivity.BUTTON_INFO)

        if (buttonInfo != null) {
            textViewDetail.text = "$buttonInfo was clicked"
        } else {
            textViewDetail.text = "No button information received."
        }
    }
}
