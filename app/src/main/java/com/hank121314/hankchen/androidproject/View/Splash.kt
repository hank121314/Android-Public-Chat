package com.hank121314.hankchen.androidproject.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.animation.*
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.TABLENAME
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userInfo
import com.hank121314.hankchen.androidproject.View.Dashboard.Dashboard
import com.hank121314.hankchen.androidproject.services.socket
import org.jetbrains.anko.*
import org.jetbrains.anko.db.*




/**
 * Created by hankchen on 2017/12/19.
 */
class Splash: AppCompatActivity() {
    val socketIO= socket();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socketIO.socketIO.connect()
        verticalLayout {
            gravity=Gravity.CENTER
            textView{
                id=R.id.Splash
                text="Public Chat"
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
                            UserInfoConstants.PASSWORD to TEXT)
                    val result = userinfo.readableDatabase.select(TABLENAME)
                    val rowParser = classParser<userInfo>()
                    val parser = rowParser { username: String, password: String -> Pair(username, password) }
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