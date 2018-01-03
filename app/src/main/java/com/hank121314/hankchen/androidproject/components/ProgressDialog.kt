package com.hank121314.hankchen.androidproject.components

import android.content.Context
import android.support.v7.app.AlertDialog
import com.hank121314.hankchen.androidproject.R

/**
 * Created by hankchen on 2017/12/14.
 */
class ProgressDialog {
    fun dialog(context: Context,title:String): AlertDialog.Builder {
        val builder= AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setView(R.layout.progress_bar)
        builder.setCancelable(false)
        return builder
    }
}