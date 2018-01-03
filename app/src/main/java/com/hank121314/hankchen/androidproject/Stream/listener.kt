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
class listener(event: String):ObservableOnSubscribe<String> {
    val socketIO=socket().socketIO
    val eventer=event
    override fun subscribe(e: ObservableEmitter<String>) {
        socketIO.on(eventer, Emitter.Listener { args ->
            Handler(Looper.getMainLooper()).post(Runnable {
                run{
                    println(args[0])
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