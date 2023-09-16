package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

class MainActivity : AppCompatActivity() {
    private val emailSeparator="@"
    private val nameSeparatorsPattern= "[.,_]"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //todo decompose
        binding.textViewName.text = parseEmail(intent.getStringExtra(emailKey))

        //todo decompose
        Glide.with(binding.imageViewUserPhoto)
            .load(R.drawable.ava2)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(binding.imageViewUserPhoto)
    }

    private fun parseEmail(email: String?): String? {
        val name = email?.substring(
            0,
            email.indexOf(emailSeparator)
        )
        return name?.replace(
            nameSeparatorsPattern.toRegex(), 
            " "
        )
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }
}