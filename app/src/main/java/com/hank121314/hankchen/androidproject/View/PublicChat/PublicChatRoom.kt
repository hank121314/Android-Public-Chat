package com.hank121314.hankchen.androidproject.View.PublicChat

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.ListView
import com.hank121314.hankchen.androidproject.Modal.Binder
import com.hank121314.hankchen.androidproject.Modal.chatRoomModel
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.parseUserInfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.Stream.listener
import com.hank121314.hankchen.androidproject.Stream.sender
import com.hank121314.hankchen.androidproject.ViewModal.FetchingMessage
import com.hank121314.hankchen.androidproject.ViewModal.sendingMessage
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.textChangedListener
import org.json.JSONObject
import java.util.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.hank121314.hankchen.androidproject.ViewModal.sendingMessageLocal
import kotlin.collections.HashMap
import android.widget.TextView




/**
 * Created by hankchen on 2018/1/2.
 */
class PublicChatRoom:AppCompatActivity (){
    lateinit var model: chatRoomModel
    lateinit var result:SelectQueryBuilder
    var listData= arrayListOf<Map<String,String>>()
    lateinit var listview:PublicListAdapter
    lateinit var listView:ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        listview=PublicListAdapter(this,listData)
        model= chatRoomModel(Binder(""),Binder(HashMap<String,String>()),Binder(true))
        val self=this
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
                        val parser = parseUserInfo()
                        val room = intent.getStringExtra("boardsName")
                        val item=HashMap<String,String>()
                        val timestamp="${Date().time}"
                        val username=result.parseList(parser).toList()[0].first.toString()
                        val name=result.parseList(parser).toList()[0].second.toString()
                        val id =UUID.randomUUID()
                        item.put("boards", room)
                        item.put("timestamp", Date(timestamp.toLong()).toLocaleString())
                        item.put("user",name)
                        item.put("image", username)
                        item.put("message", model.bindText.item)
                        item.put("sent",false.toString())
                        model.sending.item=item
                        listData.add(item)
                        listview.notifyDataSetChanged()
                        val param = JSONObject()
                        val obj = JSONObject()
                        obj.put("room", room)
                        obj.put("send", username)
                        obj.put("user",name )
                        obj.put("timestamp", timestamp)
                        obj.put("message",model.bindText.item)
                        param.put("parm", obj)
                        sender().rpc("sendMessage",param)
                        message.text=SpannableStringBuilder("")
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
        val buidler = ProgressDialog().dialog(this,"Loading...").create()
        buidler.show()
        val self=this
        val room=intent.getStringExtra("boardsName")
        listView=findViewById<ListView>(R.id.PublicChat)
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
        joinRoomStream.subscribe{ _ ->fetchMessageStream.subscribe(FetchingMessage(listView,self,buidler))}
        val inputStream = Observable.create(listener("sendMessageResponse"))
        inputStream.subscribe(sendingMessageLocal(listView, self))
    }

    override fun onResume() {
        super.onResume()
        listView.adapter=listview
        listview.notifyDataSetChanged()
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

    fun updateView(index: Int) {
        val v = listView.getChildAt(index - listView.getFirstVisiblePosition()) ?: return
        val someText = v.findViewById(R.id.createdView) as TextView
        someText.text = "Hi! I updated you manually!"
    }
}