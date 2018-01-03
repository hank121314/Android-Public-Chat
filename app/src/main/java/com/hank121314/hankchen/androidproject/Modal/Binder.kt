package com.hank121314.hankchen.androidproject.Modal

import android.view.View
import kotlin.properties.Delegates

/**
 * Created by hankchen on 2017/12/29.
 */
class Binder<T>(initValue:T) {
    private val bound: MutableMap<Int, (item: T) -> Unit> = HashMap()
    var item: T by Delegates.observable(initValue) {props,old,new->if(old!=new) bound.values.forEach { it(new) } }
    fun bind(id:Int,binding:(item:T)->Unit){
        bound.put(id,binding)
        binding(item)
    }
    fun unBind(id:Int){
        bound.remove(id)
    }
}
