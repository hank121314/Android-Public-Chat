package com.hank121314.hankchen.androidproject.ViewModal

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.gson.JsonParser
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.Stream.imageUploaderUser
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.helper.Validation
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import java.util.*

/**
 * Created by hankchen on 2018/1/2.
 */
class Register_onPress(){
    fun onRegister(activity: AppCompatActivity, buidler: AlertDialog,bitmap: Bitmap,param:JSONObject) {
        val networkError=activity.alert("NetWork Error!") {
            title = "Warning"
            yesButton { activity.toast("Please Input right name and password")
                buidler.dismiss()
            }
        }
        val username = param.getJSONObject("parm").get("username").toString()
        val password = param.getJSONObject("parm").get("password").toString()
        val name = param.getJSONObject("parm").get("name").toString()
        val birth=param.getJSONObject("parm").get("birth").toString()
        val arr=arrayOf<String>(username,password,birth,name)
        if(Validation().validateEmpty(arr)){
            buidler.dismiss()
            activity.alert("input Fields is Empty") {
                title = "Warning"
                yesButton { activity.toast("Please Input all fields")
                }
            }.show()
            return;
        }
        if(Validation().validateEmail(username)){
            buidler.dismiss()
            activity.alert("E-mail invalid") {
                title = "Warning"
                yesButton { activity.toast("Please Input validate e-mail address")
                }
            }.show()
            return;
        }
        if(Validation().validateBirth(birth)){
            buidler.dismiss()
            activity.alert("BirthDay invalid(dd/mm/yyyy)") {
                title = "Warning"
                yesButton { activity.toast("Please Input validate e-mail address")
                }
            }.show()
            return;
        }
        buidler.show()
        val register = Observable.create(RPC("register", param))
        register.subscribe(Register_VM(activity, buidler, bitmap))
    }
}
class Register_VM(activity: AppCompatActivity, buidler: AlertDialog,bitmap: Bitmap):Observer<String>{
    val activity=activity
    val buidler=buidler
    val bitmap=bitmap
    override fun onComplete() {
        return;
    }
    override fun onError(e: Throwable) {
        buidler.dismiss()
        val message = e.toString().replace("java.lang.Throwable:","")
        activity.alert(message) {
            title="Warning"
            yesButton { activity.toast("Please input new Boards name") }
        }.show()
        return;
    }

    override fun onNext(t: String) {
        val parser = JsonParser()
        val data = parser.parse(t)
        val dataValue =data.asJsonObject.get("fulfillmentValue").asJsonObject.get("data").asJsonObject
        val single = Observable.create(imageUploaderUser(bitmap, "${dataValue.get("username").toString()}-${Date().time}"))
        single.subscribe { s->
            println(s)
            println(dataValue)
            val values = ContentValues()
            values.put("username",dataValue.get("username").toString())
            values.put("password",dataValue.get("password").toString())
            values.put("name",dataValue.get("name").toString())
            activity.userinfo.writableDatabase.createTable(UserInfoConstants.TABLENAME, true,
                    UserInfoConstants.USERNAME to TEXT,
                    UserInfoConstants.NAME to TEXT,
                    UserInfoConstants.PASSWORD to TEXT)
            activity.userinfo.writableDatabase.insert(UserInfoConstants.TABLENAME,null,values)
            buidler.dismiss()
            val intent = Intent(activity, Dashboard::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onSubscribe(d: Disposable) {
        return;
    }
}