package com.example.level2.presentation.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.level2.R

// TODO: name of file
fun ImageView.setPhoto(photo: String = R.drawable.baseline_person_24.toString()) {
    Glide.with(this)
        .load(photo)
        .circleCrop()
        .placeholder(R.drawable.baseline_person_24)
        .into(this)
}