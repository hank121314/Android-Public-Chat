package com.hank121314.hankchen.androidproject.SQLite.UserInfo

/**
 * Created by hankchen on 2017/12/19.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.TABLENAME
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.USERNAME
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.PASSWORD
import com.hank121314.hankchen.androidproject.SQLite.UserInfo.UserInfoConstants.Companion.NAME

import org.jetbrains.anko.db.*


class UserInfoDB(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PublicChat", null, 1) {
    companion object {
        private var instance: UserInfoDB?
                = null

        @Synchronized
        fun getInstance(ctx: Context): UserInfoDB {
            if (instance == null) {
                instance = UserInfoDB(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(TABLENAME, true,
                USERNAME to TEXT,
                NAME to TEXT,
                PASSWORD to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
val Context.userinfo: UserInfoDB
    get() = UserInfoDB.getInstance(getApplicationContext())