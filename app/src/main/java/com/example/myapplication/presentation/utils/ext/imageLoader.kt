package com.example.myapplication.presentation.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.R


fun ImageView.setPhoto(photo:String= R.drawable.standard_contact_image.toString())
{
    Glide.with(this)
        .load(photo)
        .circleCrop()
        .placeholder(R.drawable.standard_contact_image)
        .into(this)
}