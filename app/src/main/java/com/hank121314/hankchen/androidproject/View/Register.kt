package com.hank121314.hankchen.androidproject.View

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.hank121314.hankchen.androidproject.Modal.Binder
import com.hank121314.hankchen.androidproject.Modal.registerModel
import com.hank121314.hankchen.androidproject.R
import com.hank121314.hankchen.androidproject.Stream.RPC
import com.hank121314.hankchen.androidproject.Stream.imageUploaderBoards
import com.hank121314.hankchen.androidproject.Stream.imageUploaderUser
import com.hank121314.hankchen.androidproject.ViewModal.Register_VM
import com.hank121314.hankchen.androidproject.ViewModal.Register_onPress
import com.hank121314.hankchen.androidproject.bind
import com.hank121314.hankchen.androidproject.components.ProgressDialog
import com.hank121314.hankchen.androidproject.helper.LargeBitmap
import com.hank121314.hankchen.androidproject.unBind
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.json.JSONObject
import java.util.*

/**
 * Created by hankchen on 2018/1/2.
 */
class Register:AppCompatActivity() {
    val ACTION_CAMERA_REQUEST_CODE=100
    val ACTION_ALBUM_REQUEST_CODE=200
    lateinit var model:registerModel
    override fun onCreate(savedInstanceState: Bundle?) {
        model= registerModel(Binder(""),Binder(""),Binder(""),Binder(""),Binder(RoundedBitmapDrawableFactory.create(this.resources,LargeBitmap().decodeSampledBitmapFromResource(this.applicationContext.resources,R.drawable.wood1,100,100))),Binder(true))
        super.onCreate(savedInstanceState)
        val iconfont = Typeface.createFromAsset(assets, "iconfont.ttf")
        this.title="Register"
        val buidler = ProgressDialog().dialog(this,"Logging!!").create()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels-1200
        model.image.item.cornerRadius=100F
        val self=this
        scrollView {
            verticalLayout {
                gravity=Gravity.CENTER
                padding = dip(40)
                val alert = alert {
                    customView {
                        verticalLayout {
                            textView {
                                text = "Choose Image Source"
                                textSize = 24f
                            }.lparams() {
                                bottomMargin = 30
                            }
                            linearLayout {
                                gravity = Gravity.CENTER
                                button("album") {
                                    onClick {
                                        val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                        startActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE)
                                    }
                                }
                                button("Camera") {
                                    onClick {
                                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
                                    }
                                }
                            }
                        }
                    }
                }
                val userImage = imageView {
                    id = R.id.register_ImageView
                    bind(model.bindOn) {
                        when (it) {
                            true -> bind(model.image) { this.setImageDrawable(it) }
                            false -> unBind(model.image)
                        }
                    }
                }.lparams(width = matchParent,height = height) {
                    bottomMargin = 100
                }
                textView {
                    textSize = 22f
                    text = "${resources.getString(R.string.profile)}Name"
                    typeface = iconfont
                }
                val name = editText {
                    singleLine = true
                    hint = "Name"
                }
                textView {
                    textSize = 22f
                    text = "${resources.getString(R.string.username_icon)}Username"
                    typeface = iconfont
                }
                val username = editText {
                    singleLine = true
                    hint = "e-mail"
                }.lparams(width = matchParent) {
                    topMargin = 20
                }
                textView {
                    textSize = 22f
                    text = "${resources.getString(R.string.password_icon)}Password"
                    typeface = iconfont
                }.lparams(width = matchParent) {
                    topMargin = 70
                }
                val password = editText {
                    hint = "password"
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }.lparams(width = matchParent) {
                    topMargin = 20
                }
                textView {
                    textSize = 22f
                    text = "${resources.getString(R.string.calendar)}BirthDay"
                    typeface = iconfont
                }.lparams(width = matchParent) {
                    topMargin = 70
                }
                val birthDay = editText {
                    singleLine = true
                    hint = "birthDay"
                }.lparams(width = matchParent) {
                    topMargin = 20
                }
                button("Selct Avatar") {
                    backgroundResource=R.drawable.register_button
                    onClick {
                        alert.show()
                    }
                }
                button("Submit") {
                    id = R.id.loginButton
                    textSize = 18f
                    textColor = Color.WHITE
                    backgroundResource = R.drawable.login_button
                    onClick {
                        buidler.show()
                        val param = JSONObject()
                        val obj = JSONObject()
                        obj.put("username", username.text.toString())
                        obj.put("password", password.text.toString())
                        obj.put("name", name.text.toString())
                        obj.put("birth", birthDay.text.toString())
                        param.put("parm", obj);
                        Register_onPress().onRegister(self,buidler,model.image.item.bitmap,param)
                    }
                }.lparams(width = dip(220), height = dip(44)) {
                    topMargin = 80
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val image=findViewById<ImageView>(R.id.register_ImageView)
    }
    fun displayImage(display: Bitmap){
        val rounded = RoundedBitmapDrawableFactory.create(this.resources,display)
        model.image.item=rounded
        model.image.item.cornerRadius=200F
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ACTION_CAMERA_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK && data != null){
                    displayImage(data.extras.get("data") as Bitmap)
                }
            }
            ACTION_ALBUM_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val resolver = this.contentResolver
                    val bitmap = MediaStore.Images.Media.getBitmap(resolver, data?.data)
                    displayImage(bitmap)
                }
            }

        }
    }
}