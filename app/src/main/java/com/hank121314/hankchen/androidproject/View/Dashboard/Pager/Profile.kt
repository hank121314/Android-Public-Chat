package com.hank121314.hankchen.androidproject.View.Dashboard.Pager

import android.graphics.Typeface
import android.media.Image
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.parseUserInfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Dashboard_VM
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.FetchingBoards
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.FetchingUser
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.Pager.boardClick
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.db.SelectQueryBuilder
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk25.listeners.onClick
import org.json.JSONObject

/**
 * Created by hankchen on 2018/1/4.
 */

class Profile:Fragment() {
    lateinit var result: SelectQueryBuilder
    var fetched=false
    val socket=com.hank121314.hankchen.androidproject.services.socket().socketIO

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val iconfont = Typeface.createFromAsset((activity as Dashboard).assets, "iconfont.ttf")

        return UI{
            scrollView {
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    textView {
                        text = "${resources.getString(R.string.profile)} ${resources.getString(R.string.Profile)}"
                        textSize = 48f
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        typeface = iconfont
                    }
                    imageView {
                        id = R.id.profile_ImageView
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        adjustViewBounds = true
                    }.lparams(width = 800, height = 800) {
                        topMargin = 50
                    }
                    textView {
                        text = "${resources.getString(R.string.username_icon)} ${resources.getString(R.string.Name)}"
                        textSize = 24f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        typeface = iconfont
                    }.lparams() {
                        topMargin = 50
                    }
                    textView {
                        id = R.id.profile_name
                        textSize = 24f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    }.lparams() {
                        topMargin = 20
                    }
                    textView {
                        text = "${resources.getString(R.string.email)} ${resources.getString(R.string.username)}"
                        textSize = 24f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        typeface = iconfont
                    }.lparams() {
                        topMargin = 100
                    }
                    textView {
                        id = R.id.profile_username
                        textSize = 24f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    }.lparams() {
                        topMargin = 20
                    }
                    textView {
                        text = "${resources.getString(R.string.calendar)} ${resources.getString(R.string.Birth)}"
                        textSize = 24f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                        typeface = iconfont
                    }.lparams() {
                        topMargin = 100
                    }
                    textView {
                        id = R.id.profile_birth
                        textSize = 24f
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                    }.lparams() {
                        topMargin = 20
                    }
                    button("${resources.getString(R.string.Refresh)}${resources.getString(R.string.refresh)}") {
                        transformationMethod = null
                        backgroundResource = R.drawable.add_boards
                        typeface = iconfont
                        onClick {
                            socket.connect()
                            fetchingUserInfo()
                        }
                    }.lparams(width = 300) {
                        topMargin = 20
                    }
                    button("${resources.getString(R.string.Logout)}") {
                        backgroundResource = R.drawable.register_button
                        onClick { Dashboard_VM().onLogOut((activity as Dashboard)) }
                    }.lparams(width = 300) {
                        topMargin = 100
                    }
                }
            }
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val self =this
        if(null==savedInstanceState) {
            val buidler = ProgressDialog().dialog(activity,"Loading...").create()
            if (fetched == false) {
                doAsync {
                    (activity as Dashboard).userinfo.writableDatabase.createTable(UserInfoConstants.TABLENAME, true,
                    UserInfoConstants.USERNAME to TEXT,
                    UserInfoConstants.NAME to TEXT,
                    UserInfoConstants.PASSWORD to TEXT,UserInfoConstants.ADMIN to TEXT)
                    result = (activity as Dashboard).userinfo.readableDatabase.select(UserInfoConstants.TABLENAME)
                    onComplete {
                        val parser = parseUserInfo()
                        val param = JSONObject()
                        val obj = JSONObject()
                        obj.put("username", result.parseList(parser).toList()[0].first)
                        param.put("parm", obj)
                        val infoStream = Observable.create(RPC("getUserInfo", param))
                        infoStream.subscribe(FetchingUser((activity as Dashboard), self, buidler))
                    }
                }
            }
        }
    }
    fun fetchingUserInfo(){
        val self =this
        val buidler = ProgressDialog().dialog(activity,"Loading...").create()
        if(fetched==false) {
            buidler.show()
            doAsync {
                (activity as Dashboard).userinfo.writableDatabase.createTable(UserInfoConstants.TABLENAME, true,
                        UserInfoConstants.USERNAME to TEXT,
                        UserInfoConstants.NAME to TEXT,
                        UserInfoConstants.PASSWORD to TEXT)
                result = (activity as Dashboard).userinfo.readableDatabase.select(UserInfoConstants.TABLENAME)
                onComplete {
                    val parser =parseUserInfo()
                    val param = JSONObject()
                    val obj = JSONObject()
                    obj.put("username", result.parseList(parser).toList()[0].first)
                    param.put("parm", obj)
                    val infoStream = Observable.create(RPC("getUserInfo", param))
                    infoStream.subscribe(FetchingUser((activity as Dashboard),self,buidler))
                }
            }
        }
    }
}