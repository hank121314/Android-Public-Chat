package com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager

import android.content.Intent
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
import java.util.*


/**
 * Created by hankchen on 2017/12/21.
 */
class BoardList_VM {
}
class FetchingBoards(adapter:ListView,activity:AppCompatActivity): Observer<String> {
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
            adapter.adapter= ListAdapter(activity, arr)
            return;
        }
        for(name in dataValue){
            val boards = name as JsonObject
            val item = HashMap<String, String>()
            var board =boards.get("board").asString
            var created = Date(boards.get("created").asLong).toLocaleString()
            item.put("boards",board)
            item.put("created",created)
            val img = Observable.create(imageDownloaderBoards(activity, board))
            img.subscribe { s->
                item.put("image",s)
                arr.add(item)
                adapter.adapter= ListAdapter(activity, arr)
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
class boardClick(activity: AppCompatActivity):AdapterView.OnItemClickListener{
    val activity=activity
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val boards = view!!.findViewById<TextView>(R.id.titleView).text.toString()
        val intent = Intent(activity, PublicChatRoom::class.java)
        intent.putExtra("boardsName",boards)
        activity.startActivity(intent)
    }
}