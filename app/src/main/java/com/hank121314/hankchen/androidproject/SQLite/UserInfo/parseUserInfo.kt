package com.hank121314.hankchen.androidproject.SQLite.UserInfo

import org.jetbrains.anko.db.RowParser

/**
 * Created by hankchen on 2017/12/19.
 */
class parseUserInfo : RowParser<UserForm< String, String,String,String>> {
    override fun parseRow(columns: Array<Any?>): UserForm< String, String,String,String> {
        return UserForm(columns[0] as String,columns[1] as String,columns[2] as String,columns[3] as String)
    }
}