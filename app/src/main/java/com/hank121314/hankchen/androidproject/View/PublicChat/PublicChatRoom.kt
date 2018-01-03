package com.hank121314.hankchen.androidproject.View.PublicChat

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.ListAdapter
import android.widget.ListView
import com.hank121314.hankchen.androidproject.Modal.Binder
import com.hank121314.hankchen.androidproject.Modal.chatRoomModel
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userInfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.Stream.listener
import com.hank121314.hankchen.androidproject.Stream.sender
import com.hank121314.hankchen.androidproject.View.Dashboard.AddBoards.BoardsPictureSelect
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.View.MainActivity
import com.hank121314.hankchen.androidproject.ViewModal.FetchingMessage
import com.hank121314.hankchen.androidproject.ViewModal.sendingMessage
import com.hank121314.hankchen.androidproject.helper.LargeBitmap
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.textChangedListener
import org.json.JSONObject
import java.util.*

/**
 * Created by hankchen on 2018/1/2.
 */
class PublicChatRoom:AppCompatActivity (){
    lateinit var model: chatRoomModel
    lateinit var result:SelectQueryBuilder
    var listData= arrayListOf<Map<String,String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        model= chatRoomModel(Binder(""),Binder(true))
        doAsync {
            userinfo.writableDatabase.createTable(UserInfoConstants.TABLENAME, true,
                UserInfoConstants.USERNAME to TEXT,
                UserInfoConstants.PASSWORD to TEXT)
            result = userinfo.readableDatabase.select(UserInfoConstants.TABLENAME)
        }
        super.onCreate(savedInstanceState)
        val iconfont = Typeface.createFromAsset(assets, "iconfont.ttf")
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthPix = displayMetrics.widthPixels
        val footer = UI {
            linearLayout {
                gravity=Gravity.BOTTOM
                val message = editText {
                    hint="Input some Messages"
                    textChangedListener { onTextChanged { charSequence, p1, p2, p3 -> model.bindText.item = "$charSequence" } }
                }.lparams(width=widthPix-300)
                button{
                    text="send"
                    onClick {
                        val parser = org.jetbrains.anko.db.rowParser { username: String, password: String -> Pair(username, password) }
                        val room = intent.getStringExtra("boardsName")
                        val param = JSONObject()
                        val obj = JSONObject()
                        obj.put("room", room)
                        obj.put("send", result.parseList(parser).toList()[0].first)
                        obj.put("timestamp", "${Date().time}")
                        obj.put("message",model.bindText.item)
                        model.bindText.item=""
                        message.setText(model.bindText.item)
                        param.put("parm", obj)
                        sender().rpc("sendMessage",param)
                    }
                }
            }
        }
        verticalLayout {
            val list = listView {
                id= R.id.PublicChat
            }.lparams(width=matchParent,height = matchParent)
            list.addFooterView(footer.view)
        }
    }

    override fun onStart() {
        super.onStart()
        val room=intent.getStringExtra("boardsName")
        val listView=findViewById<ListView>(R.id.PublicChat)
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setStackFromBottom(true);
        val param=JSONObject()
        val obj=JSONObject()
        this.title= room
        obj.put("room",room)
        param.put("parm", obj)
        val joinRoom=RPC("joinChatRoom",param)
        val fetchMessage=RPC("fetchMessage",param)
        val joinRoomStream=Observable.create(joinRoom)
        val fetchMessageStream=Observable.create(fetchMessage)
        val inputStream = Observable.create(listener("sendMessageResponse"))
        inputStream.subscribe(sendingMessage(listView, this))
        joinRoomStream.subscribe{ _ ->fetchMessageStream.subscribe(FetchingMessage(listView,this))}
    }

    override fun onStop() {
        super.onStop()
        val room=intent.getStringExtra("boardsName")
        val param=JSONObject()
        val obj=JSONObject()
        obj.put("room",room)
        param.put("parm", obj)
        val leave=Observable.create(RPC("leaveChatRoom",param))
        leave.subscribe()
    }
}