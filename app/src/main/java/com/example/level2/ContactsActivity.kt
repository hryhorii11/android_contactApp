package com.example.level2



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.level2.databinding.ActivityContactsBinding



class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }




}