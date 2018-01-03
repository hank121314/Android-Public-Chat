package com.hank121314.hankchen.androidproject

/**
 * Created by hankchen on 2017/12/29.
 */
import android.view.View
import com.hank121314.hankchen.androidproject.Modal.Binder

fun <T> View.bind(binder: Binder<T>, binding: (item: T) -> Unit) = binder.bind(this.id, binding)

fun <T> View.unBind(binder: Binder<T>)=binder.unBind(this.id)