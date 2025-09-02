package com.example.helloandroid // Replace with your actual package name

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.helloandroid.R // Replace with your actual package name if different

class MainActivity : AppCompatActivity() {

    // Define a constant for the key used to pass data
    companion object {
        const val BUTTON_INFO= "button_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find your buttons by their IDs
        val button1: Button = findViewById(R.id.button_show_info_1) // Assuming 'button' is your first button's ID
        val button2: Button = findViewById(R.id.button_show_info_2)
        val button3: Button = findViewById(R.id.button_show_info_3)
        val button4: Button = findViewById(R.id.button_show_info_4)
        val button5: Button = findViewById(R.id.button_show_info_5)
        // Assuming you have a fifth button with id 'button5'
        // If not, you'll need to add it to your activity_main.xml and find it here
        // For example:
        // val button5: Button = findViewById(R.id.button5)


        // Set click listeners for each button
        button1.setOnClickListener {
            navigateToDetail("Button 1 (ID: @+id/button)") // Pass descriptive info
        }

        button2.setOnClickListener {
            navigateToDetail("Button 2 (ID: @+id/button2)")
        }

        button3.setOnClickListener {
            navigateToDetail("Button 3 (ID: @+id/button3)")
        }

        button4.setOnClickListener {
            navigateToDetail("Button 4 (ID: @+id/button4)")
        }

        button5.setOnClickListener {
            navigateToDetail("Button 5 (ID: @+id/button5)")
        }
    }

    private fun navigateToDetail(buttonInfo: String) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(BUTTON_INFO, buttonInfo)
        }
        startActivity(intent)
    }
}
