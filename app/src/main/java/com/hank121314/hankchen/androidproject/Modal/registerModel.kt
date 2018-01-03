package com.hank121314.hankchen.androidproject.Modal

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawable

/**
 * Created by hankchen on 2018/1/2.
 */
data class registerModel(val name: Binder<String>, val username: Binder<String>, val password: Binder<String>, val birthDay: Binder<String>, val image: Binder<RoundedBitmapDrawable>, val bindOn: Binder<Boolean>)