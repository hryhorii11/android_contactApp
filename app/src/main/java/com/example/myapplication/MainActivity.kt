package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        binding.NameId.text=parseEmail(intent.getStringExtra("email"))
        Glide.with(this)
            .load(R.drawable.ava2)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(binding.imageView)
    }
    private fun parseEmail(email: String?):String?
    {
        var name=email?.substring(0,email.indexOf('@'))
        return name?.replace("[.,_]".toRegex()," ")
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}