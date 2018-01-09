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
import com.hank121314.hankchen.androidproject.Modal.Binder
import com.hank121314.hankchen.androidproject.Modal.loginModel
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.ViewModal.MainActivity_onPress
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import org.jetbrains.anko.sdk25.listeners.textChangedListener

class MainActivity : AppCompatActivity() {
    val socketIO=socket();
    lateinit var model:loginModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val iconfont = Typeface.createFromAsset(assets, "iconfont.ttf")
        val self=this
        model= loginModel(Binder(""),Binder(""))
        relativeLayout{
            verticalLayout {
                setGravity(Gravity.CENTER)
                padding = dip(40)
                textView{
                    textSize=22f
                    text="${resources.getString(R.string.email)} ${getString(R.string.username)}"
                    typeface=iconfont
                }
                val username = editText{
                    hint = "${getString(R.string.username)}"
                    textSize = 20f
                    singleLine=true
                    textChangedListener { onTextChanged { charSequence, p1, p2, p3 -> model.username.item = "$charSequence" } }
                }.lparams(width= matchParent){
                    topMargin=20
                }
                textView{
                    textSize=22f
                    text="${resources.getString(R.string.password_icon)} ${getString(R.string.password)}"
                    typeface=iconfont
                }.lparams(width= matchParent){
                    topMargin=70
                }
                val password = editText {
                    hint = "${getString(R.string.password)}"
                    textSize = 20f
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    textChangedListener { onTextChanged { charSequence, p1, p2, p3 -> model.password.item = "$charSequence" } }

                }.lparams(width= matchParent){
                    topMargin=20
                }
                button("${resources.getString(R.string.login)}") {
                    id= R.id.loginButton
                    textSize = 18f
                    textColor= Color.WHITE
                    backgroundResource= R.drawable.login_button
                    onClick { MainActivity_onPress().authenticate("${username.text}","${password.text}",self)  }
                }.lparams(width = dip(220), height = dip(44)) {
                    topMargin = 80
                }
                button("${resources.getString(R.string.register)}") {
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
