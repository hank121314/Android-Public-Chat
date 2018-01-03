package com.hank121314.hankchen.androidproject.helper

import java.util.Arrays
/**
 * Created by hankchen on 2017/12/14.
 */
class Validation {
    fun validateEmail(email:String):Boolean{
       return !(Regex("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]+\$").matches( email))
    }
    fun validateEmpty(field:Array<String>):Boolean{
        var boolean =false
        for(e in field){
           boolean= "".equals(e)
        }
        return boolean
    }
    fun validateBirth(birth:String):Boolean{
        return !(Regex("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$").matches(birth))
    }
}