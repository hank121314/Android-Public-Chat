package com.hank121314.hankchen.androidproject.ViewModal

import android.support.annotation.UiThread
import android.support.v7.app.AlertDialog
import android.widget.ListView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hank121314.hankchen.androidproject.Stream.imageDownloaderUser
import com.hank121314.hankchen.androidproject.View.PublicChat.PublicChatRoom
import com.hank121314.hankchen.androidproject.View.PublicChat.PublicListAdapter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by hankchen on 2018/1/2.
 */
class FetchingMessage(adapter: ListView, activity: PublicChatRoom,alert:AlertDialog): Observer<String> {
    var adapter =adapter
    val activity=activity
    val alert=alert
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data=parser.parse(t)
        val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonArray
        if(dataValue.size()==0){
            activity.listview = PublicListAdapter(activity, activity.listData)
            adapter.adapter = activity.listview
            alert.dismiss()
            return;
        }
        else {
            if (dataValue.size() - 1 > 20) {
                var k =0
                    for (i in dataValue.size() - 21 until dataValue.size()-1) {
                        val boards = dataValue[i] as JsonObject
                        val item = HashMap<String, String>()
                        var room = boards.get("room").asString
                        var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
                        var send = boards.get("send").asString
                        var message = boards.get("message").asString
                        var user = boards.get("user").asString
                        val imageStream = Observable.create(imageDownloaderUser(activity, send))
                        imageStream.subscribe { s ->
                            item.put("boards", room)
                            item.put("timestamp", timestamp)
                            item.put("user",user)
                            item.put("image", s)
                            item.put("message", message)
                            item.put("sent",true.toString())
                            activity.listData.add(item)
                            activity.listData.sortBy { it["timestamp"] }
                            if (k == dataValue.size() - 1) {
                                activity.listview = PublicListAdapter(activity, activity.listData)
                                adapter.adapter = activity.listview
                                adapter.setSelection(adapter.adapter.getCount() - 1)
                                alert.dismiss()
                            }
                            k++
                        }
                }
            } else {
                var i = 0
                    for (name in dataValue) {
                        val boards = name as JsonObject
                        val item = HashMap<String, String>()
                        var room = boards.get("room").asString
                        var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
                        var send = boards.get("send").asString
                        var message = boards.get("message").asString
                        var user = boards.get("user").asString
                        val imageStream = Observable.create(imageDownloaderUser(activity, send))
                        imageStream.subscribe { s ->
                            item.put("boards", room)
                            item.put("timestamp", timestamp)
                            item.put("image", s)
                            item.put("user",user)
                            item.put("message", message)
                            item.put("sent",true.toString())
                            activity.listData.add(item)
                            activity.listData.sortBy { it["timestamp"] }
                            if(i==dataValue.size()-1) {
                                activity.listview = PublicListAdapter(activity, activity.listData)
                                adapter.adapter = activity.listview
                                adapter.setSelection(adapter.adapter.getCount() - 1)
                                alert.dismiss()
                            }
                            i++
                        }
                }
            }
        }

    }

    override fun onError(e: Throwable) {
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}
class sendingMessageLocal(adapter: ListView, activity: PublicChatRoom): Observer<String> {
    val activity=activity
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data = parser.parse(t)
        val dataValue = data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonObject
        val boards = dataValue as JsonObject
        val item : MutableMap<String,String> = mutableMapOf()
        var room = boards.get("room").asString
        var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
        var send = boards.get("send").asString
        var message = boards.get("message").asString
        var user = boards.get("user").asString
        val imageStream = Observable.create(imageDownloaderUser(activity, send))
        imageStream.subscribe { s ->
            item.put("boards", room)
            item.put("timestamp", timestamp)
            item.put("user",user)
            item.put("image", s)
            item.put("message", message)
            item.put("sent",true.toString())
            val counter=activity.listData.indexOf(activity.model.sending.item)
            if(counter!=-1) {
                activity.listData[counter] = item
                activity.listview.notifyDataSetChanged()
            }
            else{
                activity.listData.add(item)
                activity.listview.notifyDataSetChanged()
            }
        }
    }

    override fun onError(e: Throwable) {
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}
class sendingMessage(adapter: ListView, activity: PublicChatRoom): Observer<String> {
    val activity=activity
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data = parser.parse(t)
        val dataValue = data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonObject
        val boards = dataValue as JsonObject
        val item = HashMap<String, String>()
        var room = boards.get("room").asString
        var timestamp = Date(boards.get("timestamp").asLong).toLocaleString()
        var send = boards.get("send").asString
        var message = boards.get("message").asString
        var user = boards.get("user").asString
        var sent = boards.get("sent").asString
        val imageStream = Observable.create(imageDownloaderUser(activity, send))
            imageStream.subscribe { s ->
                item.put("boards", room)
                item.put("timestamp", timestamp)
                item.put("user",user)
                item.put("image", s)
                item.put("message", message)
                item.put("sent",sent)
                activity.listData.add(item)
                activity.listview.notifyDataSetChanged()
            }
    }

    override fun onError(e: Throwable) {
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}