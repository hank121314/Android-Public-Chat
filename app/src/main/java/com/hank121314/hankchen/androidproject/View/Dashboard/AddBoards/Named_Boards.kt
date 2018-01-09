package com.hank121314.hankchen.androidproject.View.Dashboard.AddBoards

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import com.hank121314.hankchen.androidproject.R
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import com.hank121314.hankchen.androidproject.ViewModal.Dashboard.AddBoards.onSumbitBoards
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick


/**
 * Created by hankchen on 2017/12/21.
 */
class Named_Boards:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val iconfont = Typeface.createFromAsset(assets, "iconfont.ttf")
        var byteArray = intent.getByteArrayExtra("image")
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels-800
        super.onCreate(savedInstanceState)
        val self=this
        this.title=resources.getString(R.string.nameBoard)
        verticalLayout {
            setGravity(Gravity.CENTER)
            imageView {
                id = R.id.addBoard_ImageView
                setImageBitmap(bmp)
            }.lparams(width = matchParent, height = height)
            textView{
                textSize=22f
                text="${resources.getString(R.string.dashboard_title)} ${resources.getString(R.string.board)} ${resources.getString(R.string.name)}"
                typeface=iconfont
            }
            val boradName = editText{
                hint = "${resources.getString(R.string.board)} ${resources.getString(R.string.name)}"
                textSize = 20f
                singleLine=true
            }.lparams(width= matchParent){
                topMargin=20
            }
            button(resources.getString(R.string.submit)) {
                id= R.id.loginButton
                textSize = 18f
                textColor= Color.WHITE
                backgroundResource= R.drawable.login_button
                onClick {
                    onSumbitBoards(bmp,boradName.text.toString(),self).onPress()
                }
            }.lparams(width = dip(220), height = dip(44)) {
                topMargin = 80
            }
        }
    }
}