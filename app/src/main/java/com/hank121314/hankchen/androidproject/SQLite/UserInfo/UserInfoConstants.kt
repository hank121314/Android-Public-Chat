package com.hank121314.hankchen.androidproject.SQLite.UserInfo

import android.provider.BaseColumns



/**
 * Created by hankchen on 2017/12/19.
 */
interface UserInfoConstants : BaseColumns {
    companion object {
        val TABLENAME= "UserInfo"
        val USERNAME = "username"
        val PASSWORD = "password"
        val NAME="name"
    }
}