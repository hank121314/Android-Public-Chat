package com.hank121314.hankchen.androidproject.ViewModal

import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hank121314.hankchen.androidproject.View.PublicChat.PublicChatRoom
import com.hank121314.hankchen.androidproject.View.PublicChat.publicListAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by hankchen on 2018/1/2.
 */
class FetchingMessage(adapter: ListView, activity: PublicChatRoom): Observer<String> {
    var adapter =adapter
    val activity=activity
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data=parser.parse(t)
        val arr = arrayListOf<Map<String,String>>()
        val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonArray
        if(dataValue.size()==0){
            adapter.adapter= publicListAdapter(activity, arr)
            return;
        }
        if(dataValue.size()-1>20) {
            for (i in dataValue.size() - 21 until dataValue.size()) {
                val boards = dataValue[i] as JsonObject
                val item = HashMap<String, String>()
                var room = boards.get("room").asString
                var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
                var send = boards.get("send").asString
                var message = boards.get("message").asString
                item.put("boards", room)
                item.put("timestamp", timestamp)
                item.put("send", send)
                item.put("message", message)
                arr.add(item)
            }
        }
        else{
            for (name in dataValue) {
                val boards = name as JsonObject
                val item = HashMap<String, String>()
                var room = boards.get("room").asString
                var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
                var send = boards.get("send").asString
                var message = boards.get("message").asString
                item.put("boards", room)
                item.put("timestamp", timestamp)
                item.put("send", send)
                item.put("message", message)
                arr.add(item)
            }
        }
        activity.listData=arr
        adapter.adapter=publicListAdapter(activity,arr)
        adapter.setSelection(adapter.adapter.getCount() - 1)
    }

    override fun onError(e: Throwable) {
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}
class sendingMessage(adapter: ListView, activity: PublicChatRoom): Observer<String> {
    var adapter =adapter
    val activity=activity
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data=parser.parse(t)
        val arr = activity.listData
        val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonObject
        val boards = dataValue as JsonObject
        val item = HashMap<String, String>()
        var room =boards.get("room").asString
        var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
        var send =boards.get("send").asString
        var message =boards.get("message").asString
        item.put("boards",room)
        item.put("timestamp",timestamp)
        item.put("send",send)
        item.put("message",message)
        arr.add(item)
        adapter.adapter=publicListAdapter(activity,arr)
        adapter.setSelection(adapter.adapter.getCount() - 1)
    }

    override fun onError(e: Throwable) {
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}