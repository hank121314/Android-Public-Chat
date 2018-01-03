package com.hank121314.hankchen.androidproject.Stream

import android.os.Handler
import android.os.Looper
import com.github.nkzawa.emitter.Emitter
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import org.json.JSONObject
import com.hank121314.hankchen.androidproject.services.socket

/**
 * Created by hankchen on 2017/12/14.
 */
class sender {

    val socketIO=socket().socketIO

    fun rpc(event: String, param: JSONObject){
        socketIO.emit(event, param)
    }
}