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
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.View.Dashboard.Adapter.ListAdapter
import com.hank121314.hankchen.androidproject.View.Dashboard.AddBoards.BoardsPictureSelect
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.FetchingBoards
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.boardClick
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import com.hank121314.hankchen.androidproject.helper.NetWorkError
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.find
import org.json.JSONObject


/**
 * Created by hankchen on 2017/12/19.
 */
class BoardList:Fragment() {
    val socket=com.hank121314.hankchen.androidproject.services.socket().socketIO

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val iconfont = Typeface.createFromAsset((activity as Dashboard).assets, "iconfont.ttf")
        val arr = arrayListOf<Map<String,String>>()
        return UI {
            verticalLayout {
                linearLayout {
                    textView {
                        text = "${resources.getString(R.string.dashboard_title)} Boards"
                        textSize = 48f
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        typeface = iconfont
                    }
                    button("Add${resources.getString(R.string.add)}") {
                        transformationMethod=null
                        backgroundResource=R.drawable.add_boards
                        typeface = iconfont
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
                    button("Refresh${resources.getString(R.string.refresh)}") {
                        transformationMethod = null
                        backgroundResource = R.drawable.add_boards
                        typeface = iconfont
                        onClick {
                            socket.connect()
                            val alert = ProgressDialog().dialog(this.context,"Downloading Necessary File...").create()
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
            val alert = ProgressDialog().dialog(this.context,"Downloading Necessary File...").create()
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