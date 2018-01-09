package com.hank121314.hankchen.androidproject.helper

import android.support.v7.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.hank121314.hankchen.androidproject.services.socket
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton


/**
 * Created by hankchen on 2018/1/9.
 */
class NetWorkError{
    val socket =socket().socketIO
    fun show(activity:AppCompatActivity,cb:()->Unit){
        val networkError=activity.alert("NetWork Error!") {
            title = "Warning"
            yesButton { cb() }
        }
        socket.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener { args ->activity.runOnUiThread(Runnable {
            run {
                socket.off(Socket.EVENT_CONNECT_ERROR)
                networkError.show()
            }
        })})
    }
}