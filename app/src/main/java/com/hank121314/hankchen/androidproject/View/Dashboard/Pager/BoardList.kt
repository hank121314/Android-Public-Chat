package com.hank121314.hankchen.androidproject.View.Dashboard.Pager

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.support.v4.UI
import android.widget.ListView
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.parseUserInfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.View.Dashboard.Adapter.ListAdapter
import com.hank121314.hankchen.androidproject.View.Dashboard.AddBoards.BoardsPictureSelect
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.FetchingBoards
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.FetchingUser
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.boardClick
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import com.hank121314.hankchen.androidproject.helper.NetWorkError
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.db.SelectQueryBuilder
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.find
import org.json.JSONObject


/**
 * Created by hankchen on 2017/12/19.
 */
class BoardList:Fragment() {
    val socket=com.hank121314.hankchen.androidproject.services.socket().socketIO
    lateinit var result: SelectQueryBuilder
    var Administrator=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val iconfont = Typeface.createFromAsset((activity as Dashboard).assets, "iconfont.ttf")
        (activity as Dashboard).userinfo.writableDatabase.createTable(UserInfoConstants.TABLENAME, true,
                    UserInfoConstants.USERNAME to TEXT,
                    UserInfoConstants.NAME to TEXT,
                    UserInfoConstants.PASSWORD to TEXT, UserInfoConstants.ADMIN to TEXT)
        result = (activity as Dashboard).userinfo.readableDatabase.select(UserInfoConstants.TABLENAME)
        Administrator=result.parseList(parseUserInfo()).toList()[0].forth.toBoolean()
        println(result.parseList(parseUserInfo()).toList()[0].forth.toBoolean())
        val arr = arrayListOf<Map<String,String>>()
        return UI {
            verticalLayout {
                linearLayout {
                    textView {
                        text = "${resources.getString(R.string.dashboard_title)} ${resources.getString(R.string.board)}"
                        textSize = 48f
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        typeface = iconfont
                    }
                    button("${resources.getString(R.string.Add)}${resources.getString(R.string.add)}") {
                        transformationMethod=null
                        backgroundResource=R.drawable.add_boards
                        typeface = iconfont
                        isEnabled=Administrator
                        onClick {
                            val intent = Intent((activity as Dashboard), BoardsPictureSelect::class.java)
                            startActivity(intent)
                        }
                    }.lparams(width= matchParent,height = matchParent)
                }
                val list = listView {
                    id=R.id.BoardsList
                    adapter= ListAdapter(activity, arr)
                }
                list.addFooterView(UI {
                    button("${resources.getString(R.string.Refresh)} ${resources.getString(R.string.refresh)}") {
                        transformationMethod = null
                        backgroundResource = R.drawable.add_boards
                        typeface = iconfont
                        onClick {
                            socket.connect()
                            val alert = ProgressDialog().dialog(this.context,resources.getString(R.string.downloading)).create()
                            val parm = JSONObject()
                            parm.put("parm", "")
                            val rpc = RPC("getBoards", parm)
                            val boardsFetch = Observable.create(rpc)
                            boardsFetch.subscribe(FetchingBoards(list, (activity as Dashboard),alert))
                            list.setOnItemClickListener(boardClick((activity as Dashboard)))
                        }
                    }
                }.view)
            }
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(null == savedInstanceState) {
            val alert = ProgressDialog().dialog(this.context,resources.getString(R.string.downloading)).create()
            alert.show()
            NetWorkError().show(activity as Dashboard,{alert.dismiss()})
            val parm = JSONObject()
            parm.put("parm","")
            val rpc =RPC("getBoards",parm)
            val list=find<ListView>(R.id.BoardsList)
            val boardsFetch = Observable.create(rpc)
            boardsFetch.subscribe(FetchingBoards(list,(activity as Dashboard),alert))
            list.setOnItemClickListener(boardClick((activity as Dashboard)))
        }
    }
}