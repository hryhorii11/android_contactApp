package com.example.myapplication.presentation.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.R


fun ImageView.setPhoto(photo:String= R.drawable.baseline_person_2_24.toString())
{
    Glide.with(this)
        .load(photo)
        .circleCrop()
        .placeholder(R.drawable.baseline_person_2_24)
        .into(this)
}