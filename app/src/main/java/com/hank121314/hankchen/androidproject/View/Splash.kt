package com.hank121314.hankchen.androidproject.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.animation.*
import com.facebook.stetho.Stetho
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.NAME
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.TABLENAME
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.parseUserInfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userInfo
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.services.socket
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*




/**
 * Created by hankchen on 2017/12/19.
 */
class Splash: AppCompatActivity() {
    val socket=com.hank121314.hankchen.androidproject.services.socket().socketIO
    override fun onCreate(savedInstanceState: Bundle?) {
        socket.connect()
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build())
        super.onCreate(savedInstanceState)
        verticalLayout {
            gravity=Gravity.CENTER
            textView{
                id=R.id.Splash
                text="${resources.getString(R.string.app_name)}"
                textSize=48f
                textAlignment= View.TEXT_ALIGNMENT_CENTER
            }
        }
        val view = findViewById<View>(android.R.id.content)
        val mLoadAnimation = AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_in)
        val self =this
        mLoadAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                return;
            }

            override fun onAnimationRepeat(animation: Animation?) {
                return;
            }
            override fun onAnimationEnd(animation: Animation?) {
                doAsync{
                    userinfo.writableDatabase.createTable(TABLENAME,true,
                            UserInfoConstants.USERNAME to TEXT,
                            NAME to TEXT,
                            UserInfoConstants.PASSWORD to TEXT)
                    val result = userinfo.readableDatabase.select(TABLENAME)
                    val parser = parseUserInfo()
                    onComplete {
                        uiThread {
                            if (!result.parseList(parser).toList().isEmpty()) {
                                val intent = Intent(self, Dashboard::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(self, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }

                }

            }
        })
        mLoadAnimation.duration = 1500
        view.startAnimation(mLoadAnimation)
    }
}