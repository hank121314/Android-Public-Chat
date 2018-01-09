package com.hank121314.hankchen.androidproject.SQLite.UserInfo

import org.jetbrains.anko.db.RowParser

/**
 * Created by hankchen on 2017/12/19.
 */
class parseUserInfo : RowParser<Triple< String, String,String>> {
    override fun parseRow(columns: Array<Any?>): Triple<String, String,String> {
        return Triple(columns[0] as String,columns[1] as String,columns[2] as String)
    }
}