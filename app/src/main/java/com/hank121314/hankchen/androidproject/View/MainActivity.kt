package com.hank121314.hankchen.androidproject.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hank121314.hankchen.androidproject.services.socket
import org.jetbrains.anko.*
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import org.jetbrains.anko.sdk25.listeners.onClick
import android.graphics.Typeface
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.ViewModal.MainActivity_onPress
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import org.jetbrains.anko.sdk25.listeners.textChangedListener

class MainActivity : AppCompatActivity() {
    val socketIO=socket();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socketIO.socketIO.connect()
        val iconfont = Typeface.createFromAsset(assets, "iconfont.ttf")
        val self=this
        relativeLayout{
            verticalLayout {
                setGravity(Gravity.CENTER)
                padding = dip(40)
                textView{
                    textSize=22f
                    text="${resources.getString(R.string.username_icon)}Username"
                    typeface=iconfont
                }
                val username = editText{
                    hint = "username"
                    textSize = 20f
                    singleLine=true
                }.lparams(width= matchParent){
                    topMargin=20
                }
                textView{
                    textSize=22f
                    text="${resources.getString(R.string.password_icon)}Password"
                    typeface=iconfont
                }.lparams(width= matchParent){
                    topMargin=70
                }
                val password = editText {
                    hint = "Password"
                    textSize = 20f
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }.lparams(width= matchParent){
                    topMargin=20
                }
                button("Login") {
                    id= R.id.loginButton
                    textSize = 18f
                    textColor= Color.WHITE
                    backgroundResource= R.drawable.login_button
                    onClick { MainActivity_onPress().authenticate("${username.text}","${password.text}",self)  }
                }.lparams(width = dip(220), height = dip(44)) {
                    topMargin = 80
                }
                button("Register") {
                    id= R.id.registerButton
                    textSize = 18f
                    textColor= Color.WHITE
                    backgroundResource= R.drawable.register_button
                    onClick{ MainActivity_onPress().register(self) }
                }.lparams(width = dip(220), height = dip(44)){
                    topMargin=80
                }
            }.lparams(width= matchParent,height = matchParent)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}
