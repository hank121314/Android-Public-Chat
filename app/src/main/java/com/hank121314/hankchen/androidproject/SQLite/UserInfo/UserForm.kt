package com.hank121314.hankchen.androidproject.SQLite.UserInfo

import java.io.Serializable


/**
 * Created by hankchen on 2018/1/9.
 */
public data class UserForm<out A, out B, out C,out D>(
        public val first: A,
        public val second: B,
        public val third: C,
        public val forth: D
) : Serializable {

    /**
     * Returns string representation of the [Triple] including its [first], [second] and [third] values.
     */
    public override fun toString(): String = "($first, $second, $third, $forth)"
}

/**
 * Converts this triple into a list.
 */
public fun <T> UserForm<T, T, T,T>.toList(): List<T> = listOf(first, second, third,forth)