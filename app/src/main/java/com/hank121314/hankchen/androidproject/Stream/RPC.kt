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
class RPC(event: String, param: JSONObject):ObservableOnSubscribe<String> {

    val socketIO=socket().socketIO
    val eventer=event
    val params=param

    fun rpc(event: String, param: JSONObject, onSuccess: Emitter.Listener){
        socketIO.emit(event, param)
        socketIO.once(event + "Response", onSuccess)
    }

    override fun subscribe(e: ObservableEmitter<String>) {
        rpc(eventer,params, Emitter.Listener { args ->
            Handler(Looper.getMainLooper()).post(Runnable {
                run{
                    val data:JSONObject=args[0]as JSONObject
                    if (data.get("isFulfilled") == false) {
                        e.onError(Throwable(data.getJSONObject("rejectionReason").get("reason").toString()))
                    }
                    else{
                        e.onNext(data.toString())
                    }
                }
            })
        })
    }
}