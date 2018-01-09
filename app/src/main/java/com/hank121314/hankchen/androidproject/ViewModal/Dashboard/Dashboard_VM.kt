package com.hank121314.hankchen.androidproject.ViewModal.Dashboard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.userinfo
import com.hank121314.hankchen.androidproject.View.MainActivity
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.uiThread

/**
 * Created by hankchen on 2017/12/19.
 */
class Dashboard_VM {
    fun onLogOut(activity: AppCompatActivity){
        val progress = ProgressDialog().dialog(activity,activity.resources.getString(R.string.Logout)).create()
        progress.show()
        doAsync {
            val result= activity.userinfo.writableDatabase.dropTable(UserInfoConstants.TABLENAME)
            onComplete { uiThread { progress.dismiss() } }
        }
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }
}