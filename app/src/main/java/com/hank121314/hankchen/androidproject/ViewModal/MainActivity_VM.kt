package com.hank121314.hankchen.androidproject.ViewModal

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.JsonParser
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.helper.Validation
import com.hank121314.hankchen.androidproject.services.socket
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.alert
import android.content.Intent
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import android.content.ContentValues
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.TABLENAME
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.View.Register
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable

/**
 * Created by hankchen on 2017/12/14.
 */
class MainActivity_socketDataStream(alert:AlertDialog,activity: AppCompatActivity):Observer<String> {

        val alert=alert

        val activity=activity

        override fun onSubscribe(d: Disposable) {
            return;
        }

        override fun onNext(t: String) {
            alert.dismiss()
            val parser = JsonParser()
            val data = parser.parse(t)
            val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonObject
            val values = ContentValues()
            values.put("username",dataValue.get("username").toString())
            values.put("password",dataValue.get("password").toString())
            values.put("name",dataValue.get("name").toString())
            values.put("Administrator",dataValue.get("Administrator").toString())
            activity.userinfo.writableDatabase.createTable(TABLENAME, true,
                    UserInfoConstants.USERNAME to TEXT,
                    UserInfoConstants.NAME to TEXT,
                    UserInfoConstants.PASSWORD to TEXT,UserInfoConstants.ADMIN to TEXT)
            activity.userinfo.writableDatabase.insert(TABLENAME,null,values)
            val intent = Intent(activity, Dashboard::class.java)
            activity.startActivity(intent)
            return;
        }

        override fun onError(e: Throwable) {
            alert.dismiss()
            val message = e.toString().replace("java.lang.Throwable:","")
            activity.alert(message) {
                title=activity.resources.getString(R.string.Warn)
                yesButton { activity.toast(activity.resources.getString(R.string.inputCorrectLogin)) }
            }.show()
            return;
        }

        override fun onComplete() {
            return;
        }
}

class MainActivity_onPress{
    val socketIO= socket()
    fun authenticate(username:String,password:String,activity: AppCompatActivity) {
        val buidler = ProgressDialog().dialog(activity,activity.resources.getString(R.string.logging)).create()
        val param = JSONObject()
        val obj = JSONObject()
        val networkError=activity.alert(activity.resources.getString(R.string.NetworkErr)) {
            title = activity.resources.getString(R.string.Warn)
            yesButton {
                buidler.dismiss()
            }
        }
        val arr=arrayOf<String>(username,password)
        if(Validation().validateEmpty(arr)){
            activity.alert(activity.resources.getString(R.string.inputEmpty)) {
                title =  activity.resources.getString(R.string.Warn)
                yesButton { activity.toast(activity.resources.getString(R.string.inputCorrectLogin))
                }
            }.show()
            return;
        }
        if(Validation().validateEmail(username)){
            activity.alert(activity.resources.getString(R.string.emailinvaild)) {
                title =  activity.resources.getString(R.string.Warn)
                yesButton { activity.toast(activity.resources.getString(R.string.inputCorrectLogin))
                }
            }.show()
            return;
        }
        else {
            buidler.show()
            socketIO.socketIO.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener { args ->activity.runOnUiThread(Runnable {
                run {
                    socketIO.socketIO.off(Socket.EVENT_CONNECT_ERROR)
                    networkError.show()
                }
            })})
            obj.put("username", username)
            obj.put("password", password)
            param.put("parm", obj);

            val rpc= RPC("login", param)

            val socketStream = Observable.create(rpc)

            socketStream.subscribe(MainActivity_socketDataStream(buidler,activity))

            return;
        }
    }
    fun register(activity: AppCompatActivity){
        val intent = Intent(activity,Register::class.java)
        activity.startActivity(intent)
    }
}