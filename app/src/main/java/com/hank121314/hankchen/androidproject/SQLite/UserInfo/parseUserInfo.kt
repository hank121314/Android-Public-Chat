package com.hank121314.hankchen.androidproject.SQLite.UserInfo

import org.jetbrains.anko.db.RowParser

/**
 * Created by hankchen on 2017/12/19.
 */
class parseUserInfo : RowParser<Pair< String, String>> {
    override fun parseRow(columns: Array<Any?>): Pair<String, String> {
        return Pair(columns[0] as String,columns[1] as String)
    }
}