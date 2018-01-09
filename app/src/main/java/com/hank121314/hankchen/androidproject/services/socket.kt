package com.hank121314.hankchen.androidproject.services

import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket

class socket{
    var socketIO: Socket= IO.socket(server_siete)
    companion object {
        val server_siete="http://www.amlispiration.com:7879"
//        val server_siete="http://10.1.212.175:7879"
    }
}
