package com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.google.gson.*
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.View.Dashboard.Adapter.ListAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import com.hank121314.hankchen.androidproject.Stream.imageDownloaderBoards
import com.hank121314.hankchen.androidproject.View.PublicChat.PublicChatRoom
import io.reactivex.Observable
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.*


/**
 * Created by hankchen on 2017/12/21.
 */
class BoardList_VM {
}
class FetchingBoards(adapter:ListView,activity:AppCompatActivity,alert: AlertDialog): Observer<String> {
    var adapter =adapter
    val activity=activity
    val alert=alert
    override fun onComplete() {
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data=parser.parse(t)
        val arr = arrayListOf<Map<String,String>>()
        val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonArray
        if(dataValue.size()==0){
            adapter.adapter= ListAdapter(activity, arr)
            alert.dismiss()
            return;
        }
        for(name in dataValue){
            val boards = name as JsonObject
            val item = HashMap<String, String>()
            var board =boards.get("board").asString
            var userCount =boards.get("userCount").asInt
            var created = Date(boards.get("created").asLong).toLocaleString()
            item.put("boards",board)
            item.put("created",created)
            item.put("userCount",userCount.toString())
            val img = Observable.create(imageDownloaderBoards(activity, board))
            img.subscribe { s->
                item.put("image",s)
                arr.add(item)
                adapter.adapter= ListAdapter(activity, arr)
                alert.dismiss()
            }
        }
    }

    override fun onError(e: Throwable) {
        alert.dismiss()
        val message = e.toString().replace("java.lang.Throwable:","")
        activity.alert(message) {
            title="Warning"
            yesButton { activity.toast("Please input correct Username and Password") }
        }.show()
        return;
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }

}
class boardClick(activity: AppCompatActivity):AdapterView.OnItemClickListener{
    val activity=activity
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val boards = view!!.findViewById<TextView>(R.id.titleView).text.toString()
        val intent = Intent(activity, PublicChatRoom::class.java)
        intent.putExtra("boardsName",boards)
        activity.startActivity(intent)
    }
}